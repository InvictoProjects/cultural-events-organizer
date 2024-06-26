package com.invictoprojects.controller

import com.invictoprojects.dto.NotificationDto
import com.invictoprojects.model.Notification
import com.invictoprojects.model.NotificationType
import com.invictoprojects.service.NotificationService
import com.invictoprojects.utils.MappingUtils
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.sse.Event
import io.micronaut.security.annotation.Secured
import jakarta.inject.Inject
import reactor.core.publisher.Flux
import java.security.Principal
import java.time.LocalDateTime


@Controller("/api/notifications")
@Secured("USER")
class NotificationController(
    @Inject private val notificationService: NotificationService
) {

    @Get(produces = [MediaType.TEXT_EVENT_STREAM])
    fun notifications(@QueryValue since: LocalDateTime? = null, principal: Principal): Flux<Event<NotificationDto>> {
        return notificationService.getNotifications(principal.name, since)
            .map { Event.of(MappingUtils.convertToDto(it)) }
    }

    @Post
    fun addNotificationForUser(@Body message: String, principal: Principal): Notification {
        return notificationService.addNotificationForUser(principal.name, message, NotificationType.INFO, null)
    }

}
