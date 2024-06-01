package com.invictoprojects.service.impl

import com.invictoprojects.model.*
import com.invictoprojects.repository.ComplaintRepository
import com.invictoprojects.service.EventService
import com.invictoprojects.service.NotificationService
import com.invictoprojects.service.TicketService
import com.invictoprojects.utils.createEvent
import com.invictoprojects.utils.createUser
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*

import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class ComplaintServiceImplTest {

    @Mock
    private lateinit var complaintRepository: ComplaintRepository

    @Mock
    private lateinit var notificationService: NotificationService

    @Mock
    private lateinit var ticketService: TicketService

    @Mock
    private lateinit var eventService: EventService

    @InjectMocks
    private lateinit var complaintService: ComplaintServiceImpl

    private lateinit var complaint: Complaint
    private lateinit var event: Event
    private lateinit var user: User

    @BeforeEach
    fun setUp() {
        user = createUser(Role.USER)
        event = createEvent(user)
        complaint = Complaint(
            status = ComplaintStatus.REPORTED,
            event = event,
            author = user,
            text = "Complaint text",
            id = 1L
        )
    }

    @Test
    fun `create should save complaint`() {
        whenever(complaintRepository.save(any())).thenReturn(complaint)

        val result = complaintService.create(complaint)

        assertEquals(complaint, result)
        verify(complaintRepository).save(complaint)
    }

    @Test
    fun `getUserComplaints should return user complaints`() {
        val complaints = listOf(complaint)
        whenever(complaintRepository.findByAuthorUsername(user.username)).thenReturn(complaints)

        val result = complaintService.getUserComplaints(user.username)

        assertEquals(complaints, result)
        verify(complaintRepository).findByAuthorUsername(user.username)
    }

    @Test
    fun `getEventComplaints should return event complaints`() {
        val complaints = listOf(complaint)
        whenever(complaintRepository.findByEventId(event.id!!)).thenReturn(complaints)

        val result = complaintService.getEventComplaints(event.id!!)

        assertEquals(complaints, result)
        verify(complaintRepository).findByEventId(event.id!!)
    }

    @Test
    fun `findById should return complaint if found`() {
        whenever(complaintRepository.findById(complaint.id!!)).thenReturn(Optional.of(complaint))

        val result = complaintService.findById(complaint.id!!)

        assertEquals(complaint, result)
        verify(complaintRepository).findById(complaint.id!!)
    }

    @Test
    fun `findById should return null if not found`() {
        whenever(complaintRepository.findById(any())).thenReturn(Optional.empty())

        val result = complaintService.findById(1L)

        assertNull(result)
        verify(complaintRepository).findById(1L)
    }

    @Test
    fun `findAll should return all complaints`() {
        val complaints = listOf(complaint)
        whenever(complaintRepository.findAll()).thenReturn(complaints)

        val result = complaintService.findAll()

        assertEquals(complaints, result)
        verify(complaintRepository).findAll()
    }

    @Test
    fun `deleteById should delete complaint`() {
        complaintService.deleteById(1L)

        verify(complaintRepository).deleteById(1L)
    }

    @Test
    fun `approve should update complaint status and notify users`() {
        whenever(complaintRepository.findById(complaint.id!!)).thenReturn(Optional.of(complaint))
        whenever(ticketService.findByEvent(any())).thenReturn(listOf())

        complaintService.approve(complaint.id!!)

        assertEquals(ComplaintStatus.APPROVED, complaint.status)
        assertTrue(event.isBlocked)
        verify(complaintRepository).update(complaint)
        verify(eventService).update(event)
        verify(notificationService).addNotificationForUser(user.username, "Thanks for noticing. Event blocked", NotificationType.COMPLIANT_APPROVED, event)
    }

    @Test
    fun `cancel should update complaint status and notify user`() {
        whenever(complaintRepository.findById(complaint.id!!)).thenReturn(Optional.of(complaint))

        complaintService.cancel(complaint.id!!)

        assertEquals(ComplaintStatus.CANCELED, complaint.status)
        verify(complaintRepository).update(complaint)
        verify(notificationService).addNotificationForUser(user.username, "Canceled", NotificationType.COMPLIANT_CANCELED, event)
    }
}
