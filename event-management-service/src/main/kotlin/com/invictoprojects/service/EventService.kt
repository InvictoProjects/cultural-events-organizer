package com.invictoprojects.service

import com.invictoprojects.model.Event
import com.invictoprojects.model.EventAnalytics
import com.invictoprojects.model.EventAnalyticsEnvelope
import java.time.LocalDateTime

interface EventService {

    fun create(event: Event): Event

    fun update(event: Event): Event

    fun findById(id: Long): Event

    fun findAll(): MutableIterable<Event>

    fun searchEvents(keywords: List<String>?, categories: List<String>?, dateFrom: LocalDateTime?, dateTo: LocalDateTime?): MutableIterable<Event>

    fun getEventAnalytics(event: Event): EventAnalytics

    fun getEventsAnalytics(dateFrom: LocalDateTime?, dateTo: LocalDateTime?): List<EventAnalyticsEnvelope>

    fun getAttendedEvent(username: String): List<Event>

    fun waitForEventTickets(eventId: Long, username: String)

    fun unWaitForEventTickets(eventId: Long, username: String)

    fun existById(id: Long): Boolean

    fun deleteById(id: Long)

}
