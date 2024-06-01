package com.invictoprojects.service.impl

import com.invictoprojects.model.*
import com.invictoprojects.repository.TicketRepository
import com.invictoprojects.service.NotificationService
import com.invictoprojects.service.WaitListService
import com.invictoprojects.utils.createEvent
import com.invictoprojects.utils.createUser
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class TicketServiceImplTest {

    @Mock
    private lateinit var ticketRepository: TicketRepository

    @Mock
    private lateinit var waitListService: WaitListService

    @Mock
    private lateinit var notificationService: NotificationService

    @InjectMocks
    private lateinit var ticketService: TicketServiceImpl

    private lateinit var event: Event
    private lateinit var user: User
    private lateinit var discountCode: DiscountCode

    @BeforeEach
    fun setUp() {
        user = createUser(Role.USER)
        event = createEvent(user, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), true)
        discountCode = DiscountCode("TESTCODE", 10.0f, event)
    }

    @Test
    fun `purchaseTickets should save tickets and handle waitlist`() {
        ticketService.purchaseTickets(event, user, 5, null, true)

        verify(ticketRepository, times(5)).save(any<Ticket>())
        verify(waitListService).deleteByEventAndUser(event, user)
    }

    @Test
    fun `purchaseTickets should throw exception if event ended`() {
        val event = createEvent(user, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1), false)

        val exception = assertThrows<IllegalArgumentException> {
            ticketService.purchaseTickets(event, user, 1, null, false)
        }

        assertEquals("Event already ended", exception.message)
    }

    @Test
    fun `purchaseTickets should throw exception if not enough tickets`() {
        whenever(ticketRepository.findByEvent(event)).thenReturn(List(100) { Ticket(event, user, Instant.now(), 100.0, TicketStatus.ACTIVE) })

        val exception = assertThrows<IllegalArgumentException> {
            ticketService.purchaseTickets(event, user, 1, null, false)
        }

        assertEquals("Not enough tickets", exception.message)
    }

    @Test
    fun `cancelById should update ticket status and notify waitlist`() {
        val ticket = Ticket(event, user, Instant.now(), 100.0, TicketStatus.ACTIVE, id = 1L)
        whenever(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket))
        whenever(waitListService.findByEvent(event)).thenReturn(listOf(WaitList(user, event)))

        ticketService.cancelById(1L)

        assertEquals(TicketStatus.CANCELED, ticket.status)
        verify(ticketRepository).update(ticket)
        verify(notificationService).addNotificationForUser(user.username, "There is new tickets for this event", NotificationType.EVENT_NEW_TICKETS, event)
    }

    @Test
    fun `cancelById should throw exception if ticket not found`() {
        whenever(ticketRepository.findById(any())).thenReturn(Optional.empty())

        val exception = assertThrows<IllegalArgumentException> {
            ticketService.cancelById(1L)
        }

        assertEquals("No ticket with such id", exception.message)
    }

    @Test
    fun `findPurchasedTicketsByUserId should return tickets`() {
        val tickets = mutableListOf(Ticket(event, user, Instant.now(), 100.0, TicketStatus.ACTIVE))
        whenever(ticketRepository.findByUserId(user.id!!)).thenReturn(tickets)

        val result = ticketService.findPurchasedTicketsByUserId(user.id!!)

        assertEquals(tickets, result)
    }

    @Test
    fun `findUsersByEvent should return users`() {
        val tickets = listOf(Ticket(event, user, Instant.now(), 100.0, TicketStatus.ACTIVE))
        whenever(ticketRepository.findByEvent(event)).thenReturn(tickets)

        val result = ticketService.findUsersByEvent(event)

        assertEquals(setOf(user), result)
    }

    @Test
    fun `findByEventAndUser should return ticket`() {
        val ticket = Ticket(event, user, Instant.now(), 100.0, TicketStatus.ACTIVE)
        whenever(ticketRepository.findByEventAndUser(event, user)).thenReturn(ticket)

        val result = ticketService.findByEventAndUser(event, user)

        assertEquals(ticket, result)
    }

    @Test
    fun `findByEvent should return tickets`() {
        val tickets = listOf(Ticket(event, user, Instant.now(), 100.0, TicketStatus.ACTIVE))
        whenever(ticketRepository.findByEvent(event)).thenReturn(tickets)

        val result = ticketService.findByEvent(event)

        assertEquals(tickets, result)
    }

    @Test
    fun `countByStatusAndEventId should return count`() {
        whenever(ticketRepository.countByStatusAndEventId(TicketStatus.ACTIVE, event.id!!)).thenReturn(10L)

        val result = ticketService.countByStatusAndEventId(TicketStatus.ACTIVE, event.id!!)

        assertEquals(10L, result)
    }
}
