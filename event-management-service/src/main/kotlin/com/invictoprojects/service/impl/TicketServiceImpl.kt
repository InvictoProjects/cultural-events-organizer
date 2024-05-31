package com.invictoprojects.service.impl

import com.invictoprojects.model.*
import com.invictoprojects.repository.TicketRepository
import com.invictoprojects.service.NotificationService
import com.invictoprojects.service.TicketService
import com.invictoprojects.service.WaitListService
import jakarta.inject.Inject
import jakarta.inject.Singleton
import java.time.Instant
import java.time.LocalDateTime
import java.util.stream.Collectors


@Singleton
class TicketServiceImpl(
    @Inject private val ticketRepository: TicketRepository,
    @Inject private val waitListService: WaitListService,
    @Inject private val notificationService: NotificationService
) : TicketService {

    override fun purchaseTickets(event: Event, user: User, amount: Long, isUnSubscribeFromWaitingList: Boolean) {
        if (event.endTime < LocalDateTime.now()) {
            throw IllegalArgumentException("Event already ended")
        }
        if (event.isTicketsLimited && ticketRepository.findByEvent(event).size + amount > event.maxTickets!!) {
            throw IllegalArgumentException("Not enough tickets")
        }
        for (i in 1..amount) {
            val ticket = Ticket(event, user, Instant.now(), TicketStatus.ACTIVE)
            ticketRepository.save(ticket)
        }
        if (isUnSubscribeFromWaitingList) {
            waitListService.deleteByEventAndUser(event, user)
        }
    }

    override fun findPurchasedTicketsByUserId(userId: Long): MutableIterable<Ticket> {
        return ticketRepository.findByUserId(userId)
    }

    override fun findUsersByEvent(event: Event): Set<User> {
        return ticketRepository.findByEvent(event).stream()
            .map { ticket -> ticket.user }
            .collect(Collectors.toSet())
    }

    override fun findByEventAndUser(event: Event, user: User): Ticket? {
        return ticketRepository.findByEventAndUser(event, user)
    }

    override fun countByStatusAndEventId(status: TicketStatus, eventId: Long): Long {
        return ticketRepository.countByStatusAndEventId(status, eventId)
    }

    override fun cancelById(id: Long) {
        val optionalTicket = ticketRepository.findById(id)
        if (optionalTicket.isEmpty) {
            throw IllegalArgumentException("No ticket with such id")
        }
        val ticket = optionalTicket.get()
        ticket.status = TicketStatus.CANCELED
        val event = ticket.event
        if (event.isTicketsLimited) {
            waitListService.findByEvent(event).stream()
                .map { w -> w.user }
                .forEach { user -> notificationService.addNotificationForUser(user.username, "There is new tickets for this event", NotificationType.EVENT_NEW_TICKETS, event) }
        }
        ticketRepository.update(ticket)
    }

}
