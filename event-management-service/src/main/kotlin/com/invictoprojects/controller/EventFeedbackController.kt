package com.invictoprojects.controller

import com.invictoprojects.dto.EventFeedbackDto
import com.invictoprojects.service.EventFeedbackService
import com.invictoprojects.service.EventService
import com.invictoprojects.service.TicketService
import com.invictoprojects.service.UserService
import com.invictoprojects.utils.MappingUtils
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import jakarta.inject.Inject
import java.security.Principal
import java.time.LocalDateTime

@Controller("/api/feedbacks")
@Secured("USER")
class EventFeedbackController(
    @Inject private val eventService: EventService,
    @Inject private val ticketService: TicketService,
    @Inject private val userService: UserService,
    @Inject private val eventFeedbackService: EventFeedbackService,
) {

    @Post
    fun leaveFeedback(@Body eventFeedbackDto: EventFeedbackDto, principal: Principal): HttpResponse<Any> {
        val user = userService.findByUsername(principal.name)
        if (user != null) {
            if (user.blocked) {
                return HttpResponse.status(HttpStatus.FORBIDDEN)
            }
            val event = eventService.findById(eventFeedbackDto.eventId)
            if (LocalDateTime.now() < event.endTime) {
                throw IllegalArgumentException("Event not ended")
            }
            ticketService.findByEventAndUser(event, user)
                ?: throw IllegalArgumentException("User didn't purchase ticket for this event")
            val eventFeedback = MappingUtils.convertToEntity(eventFeedbackDto, user, event)
            eventFeedbackService.leaveFeedback(eventFeedback)
            return HttpResponse.ok()
        }
        return HttpResponse.badRequest()
    }

}
