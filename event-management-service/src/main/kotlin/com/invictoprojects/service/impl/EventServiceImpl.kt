package com.invictoprojects.service.impl

import com.invictoprojects.model.Event
import com.invictoprojects.model.NotificationType
import com.invictoprojects.repository.EventRepository
import com.invictoprojects.service.EventService
import com.invictoprojects.service.NotificationService
import com.invictoprojects.service.TicketService
import jakarta.inject.Inject
import jakarta.inject.Singleton
import java.time.LocalDateTime
import javax.transaction.Transactional

@Singleton
open class EventServiceImpl(
    @Inject private val eventRepository: EventRepository,
    @Inject private val ticketService: TicketService,
    @Inject private val notificationService: NotificationService
) : EventService {

    override fun create(event: Event): Event {
        return eventRepository.save(event)
    }

    @Transactional
    override fun update(event: Event): Event {
        val updatedEvent = eventRepository.update(event)
        ticketService.findUsersByEvent(updatedEvent).stream()
            .forEach { user -> notificationService.addNotificationForUser(user.username, "Updated event", NotificationType.EVENT_UPDATE, event) }
        return updatedEvent
    }

    override fun findById(id: Long): Event {
        val event = eventRepository.findById(id)
        return event.get()
    }

    override fun findAll(): MutableIterable<Event> {
        return eventRepository.findAll()
    }

    override fun searchEvents(keywords: List<String>?, dateFrom: LocalDateTime?, dateTo: LocalDateTime?): MutableIterable<Event> {
        if (keywords != null) {
            val keywordsPattern = keywords.joinToString("|", prefix = "(", postfix = ")") { ".*%$it%.*" }
            return eventRepository.searchEventsByStartTimeBetweenAndKeywords(dateFrom, dateTo, keywordsPattern)
        }
        return eventRepository.searchByStartTimeBetween(dateFrom, dateTo)
    }

    override fun existById(id: Long): Boolean {
        return eventRepository.existsById(id)
    }

    override fun deleteById(id: Long) {
        eventRepository.deleteById(id)
    }

}
