package com.invictoprojects.service.impl

import com.invictoprojects.model.*
import com.invictoprojects.repository.EventRepository
import com.invictoprojects.service.*
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
class EventServiceImplTest {

    @Mock
    private lateinit var eventRepository: EventRepository

    @Mock
    private lateinit var ticketService: TicketService

    @Mock
    private lateinit var notificationService: NotificationService

    @Mock
    private lateinit var feedbackService: EventFeedbackService

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var waitListService: WaitListService

    @InjectMocks
    private lateinit var eventService: EventServiceImpl

    private lateinit var event: Event
    private lateinit var user: User

    @BeforeEach
    fun setUp() {
        user = createUser(Role.USER)
        event = createEvent(user)
    }

    @Test
    fun `create should save event`() {
        whenever(eventRepository.save(any())).thenReturn(event)

        val result = eventService.create(event)

        assertEquals(event, result)
        verify(eventRepository).save(event)
    }

    @Test
    fun `update should update event and notify users`() {
        whenever(eventRepository.update(any())).thenReturn(event)
        whenever(ticketService.findUsersByEvent(any())).thenReturn(setOf(user))

        val result = eventService.update(event)

        assertEquals(event, result)
        verify(eventRepository).update(event)
        verify(notificationService).addNotificationForUser(user.username, "Updated event", NotificationType.EVENT_UPDATE, event)
    }

    @Test
    fun `findById should return event if found`() {
        whenever(eventRepository.findById(event.id!!)).thenReturn(Optional.of(event))

        val result = eventService.findById(event.id!!)

        assertEquals(event, result)
        verify(eventRepository).findById(event.id!!)
    }

    @Test
    fun `findById should throw exception if not found`() {
        whenever(eventRepository.findById(any())).thenReturn(Optional.empty())

        assertThrows<NoSuchElementException> {
            eventService.findById(1L)
        }
        verify(eventRepository).findById(1L)
    }

    @Test
    fun `findAll should return all events`() {
        val events = listOf(event)
        whenever(eventRepository.findAll()).thenReturn(events)

        val result = eventService.findAll()

        assertEquals(events, result)
        verify(eventRepository).findAll()
    }

    @Test
    fun `searchEvents should return events based on search criteria`() {
        val events = mutableListOf(event)
        val keywords = listOf("Test")
        val categories = listOf("Category")
        whenever(eventRepository.searchEventsByStartTimeBetweenAndKeywords(any(), any(), any(), any())).thenReturn(events)

        val result = eventService.searchEvents(keywords, categories, LocalDateTime.now(), LocalDateTime.now().plusDays(1))

        assertEquals(events, result)
        verify(eventRepository).searchEventsByStartTimeBetweenAndKeywords(any(), any(), any(), any())
    }

    @Test
    fun `getEventAnalytics should return event analytics`() {
        whenever(ticketService.countByStatusAndEventId(TicketStatus.ACTIVE, event.id!!)).thenReturn(10L)
        whenever(ticketService.countByStatusAndEventId(TicketStatus.CANCELED, event.id!!)).thenReturn(2L)
        whenever(feedbackService.countEventIdWithNonEmptyFeedback(event.id!!)).thenReturn(5L)
        whenever(feedbackService.getAvgRateByEventId(event.id!!)).thenReturn(4.5f)

        val analytics = eventService.getEventAnalytics(event)

        assertEquals(10L, analytics.activeTickets)
        assertEquals(2L, analytics.canceledTickets)
        assertEquals(5L, analytics.feedbacks)
        assertEquals(4.5f, analytics.avrRate)
    }

    @Test
    fun `getEventsAnalytics should return analytics for events`() {
        val events = mutableListOf(event)
        val dateFrom = LocalDateTime.now().minusDays(1)
        val dateTo = LocalDateTime.now().plusDays(1)
        whenever(eventRepository.searchByStartTimeBetween(dateFrom, dateTo, null)).thenReturn(events)
        whenever(ticketService.countByStatusAndEventId(TicketStatus.ACTIVE, event.id!!)).thenReturn(10L)
        whenever(ticketService.countByStatusAndEventId(TicketStatus.CANCELED, event.id!!)).thenReturn(2L)
        whenever(feedbackService.countEventIdWithNonEmptyFeedback(event.id!!)).thenReturn(5L)
        whenever(feedbackService.getAvgRateByEventId(event.id!!)).thenReturn(4.5f)

        val analyticsEnvelopes = eventService.getEventsAnalytics(dateFrom, dateTo)

        assertEquals(1, analyticsEnvelopes.size)
        val analytics = analyticsEnvelopes[0].eventAnalytics
        assertEquals(10L, analytics.activeTickets)
        assertEquals(2L, analytics.canceledTickets)
        assertEquals(5L, analytics.feedbacks)
        assertEquals(4.5f, analytics.avrRate)
    }

    @Test
    fun `getAttendedEvent should return attended events for user`() {
        whenever(userService.findByUsername(user.username)).thenReturn(user)
        whenever(ticketService.findPurchasedTicketsByUserId(user.id!!)).thenReturn(mutableListOf(Ticket(event, user, Instant.now(), 100.0, TicketStatus.ACTIVE)))

        val result = eventService.getAttendedEvent(user.username)

        assertEquals(1, result.size)
        assertEquals(event, result[0])
    }

    @Test
    fun `waitForEventTickets should add user to waitlist`() {
        whenever(userService.findByUsername(user.username)).thenReturn(user)
        whenever(eventRepository.findById(event.id!!)).thenReturn(Optional.of(event))

        eventService.waitForEventTickets(event.id!!, user.username)

        verify(waitListService).create(event, user)
    }

    @Test
    fun `unWaitForEventTickets should remove user from waitlist`() {
        whenever(userService.findByUsername(user.username)).thenReturn(user)
        whenever(eventRepository.findById(event.id!!)).thenReturn(Optional.of(event))

        eventService.unWaitForEventTickets(event.id!!, user.username)

        verify(waitListService).deleteByEventAndUser(event, user)
    }

    @Test
    fun `existById should return true if event exists`() {
        whenever(eventRepository.existsById(event.id!!)).thenReturn(true)

        val result = eventService.existById(event.id!!)

        assertTrue(result)
        verify(eventRepository).existsById(event.id!!)
    }

    @Test
    fun `existById should return false if event does not exist`() {
        whenever(eventRepository.existsById(event.id!!)).thenReturn(false)

        val result = eventService.existById(event.id!!)

        assertFalse(result)
        verify(eventRepository).existsById(event.id!!)
    }

    @Test
    fun `deleteById should delete event`() {
        eventService.deleteById(event.id!!)

        verify(eventRepository).deleteById(event.id!!)
    }

    @Test
    fun `getByCreatorUsername should return events created by user`() {
        val events = listOf(event)
        whenever(eventRepository.findByCreatorUsername(user.username)).thenReturn(events)

        val result = eventService.getByCreatorUsername(user.username)

        assertEquals(events, result)
        verify(eventRepository).findByCreatorUsername(user.username)
    }

    @Test
    fun `findByInvitationCode should return event by invitation code`() {
        whenever(eventRepository.findByInvitationCode("INVITE123")).thenReturn(event)

        val result = eventService.findByInvitationCode("INVITE123")

        assertEquals(event, result)
        verify(eventRepository).findByInvitationCode("INVITE123")
    }
}
