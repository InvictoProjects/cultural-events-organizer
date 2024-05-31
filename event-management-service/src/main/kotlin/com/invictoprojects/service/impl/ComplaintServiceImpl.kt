package com.invictoprojects.service.impl

import com.invictoprojects.model.Complaint
import com.invictoprojects.model.ComplaintStatus
import com.invictoprojects.model.NotificationType
import com.invictoprojects.model.TicketStatus
import com.invictoprojects.repository.ComplaintRepository
import com.invictoprojects.service.CompliantService
import com.invictoprojects.service.EventService
import com.invictoprojects.service.NotificationService
import com.invictoprojects.service.TicketService
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class ComplaintServiceImpl(
    @Inject private val complaintRepository: ComplaintRepository,
    @Inject private val notificationService: NotificationService,
    @Inject private val ticketService: TicketService,
    @Inject private val eventService: EventService
) : CompliantService {
    override fun create(complaint: Complaint): Complaint {
        return complaintRepository.save(complaint)
    }

    override fun getUserComplaints(username: String): List<Complaint> {
        return complaintRepository.findByAuthorUsername(username)
    }

    override fun getEventComplaints(id: Long): List<Complaint> {
        return complaintRepository.findByEventId(id)
    }

    override fun findById(id: Long): Complaint? {
        val result = complaintRepository.findById(id)
        if (result.isEmpty) {
            return null
        }
        return result.get()
    }

    override fun findAll(): List<Complaint> {
        return complaintRepository.findAll().toList()
    }

    override fun deleteById(id: Long) {
        complaintRepository.deleteById(id)
    }

    override fun approve(id: Long) {
        val complaint = complaintRepository.findById(id).get()
        complaint.status = ComplaintStatus.APPROVED
        val event = complaint.event
        notificationService.addNotificationForUser(
            complaint.author.username,
            "Thanks for noticing. Event blocked",
            NotificationType.COMPLIANT_APPROVED,
            event
        )
        event.creators.stream()
            .map { u -> u.username }
            .forEach { username ->
                notificationService.addNotificationForUser(
                    username,
                    "Event is blocked",
                    NotificationType.EVENT_COMPLIANT,
                    event
                )
            }
        ticketService.findByEvent(event).stream()
            .map { t ->
                t.status = TicketStatus.CANCELED
                return@map t
            }.forEach {
                notificationService.addNotificationForUser(
                    it.user.username,
                    "Event is blocked",
                    NotificationType.EVENT_COMPLIANT,
                    event
                )
                ticketService.update(it)
            }
        event.isBlocked = true
        eventService.update(event)
        complaintRepository.update(complaint)
    }

    override fun cancel(id: Long) {
        val complaint = complaintRepository.findById(id).get()
        complaint.status = ComplaintStatus.CANCELED
        val event = complaint.event
        notificationService.addNotificationForUser(
            complaint.author.username,
            "Canceled",
            NotificationType.COMPLIANT_CANCELED,
            event
        )
        complaintRepository.update(complaint)
    }
}
