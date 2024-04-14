package com.invictoprojects.controller

import com.invictoprojects.dto.EventDto
import com.invictoprojects.model.Event
import com.invictoprojects.model.EventType
import com.invictoprojects.service.EventService
import com.invictoprojects.utils.MappingUtils
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import jakarta.inject.Inject
import io.micronaut.security.annotation.Secured

@Controller("/api/events")
@Secured(SecurityRule.IS_AUTHENTICATED)
class EventController(@Inject private val eventService: EventService) {

    @Post
    fun create(@Body eventDto: EventDto): HttpResponse<Any> {
        if (eventDto.startTime > eventDto.endTime) {
            throw IllegalArgumentException("Start date must be earlier than end date")
        }
        if (EventType.ONLINE == eventDto.eventType && eventDto.url == null) {
            throw IllegalArgumentException("Url must be filled for online event")
        }
        if (EventType.OFFLINE == eventDto.eventType && eventDto.location == null) {
            throw IllegalArgumentException("Location must be filled for offline event")
        }
        val event = MappingUtils.convertToEntity(eventDto)
        return HttpResponse.created(eventService.create(event))
    }


    @Put("/{id}")
    fun update(@PathVariable id: Long, @Body eventDto: EventDto): Event {
        if (!eventService.existById(id)) {
            throw IllegalArgumentException("No event with a such id")
        }
        val event = MappingUtils.convertToEntity(eventDto)
        event.id = id
        return eventService.update(event)
    }

    @Get("/{id}")
    fun findById(@PathVariable id: Long): Event {
        return eventService.findById(id)
    }

    @Get
    fun findAll(): MutableIterable<Event> = eventService.findAll()

    @Get("/search{?keywords}")
    fun searchEvents(keywords: List<String>?): MutableIterable<Event> {
        return if (keywords != null) {
            eventService.searchEventsByKeywords(keywords)
        } else {
            eventService.findAll()
        }
    }

    @Delete("/{id}")
    fun deleteById(id: Long): HttpResponse<Any> {
        eventService.deleteById(id)
        return HttpResponse.status(HttpStatus.NO_CONTENT)
    }

}
