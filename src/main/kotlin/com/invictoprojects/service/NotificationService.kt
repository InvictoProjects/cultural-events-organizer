package com.invictoprojects.service

import com.invictoprojects.model.Event
import com.invictoprojects.model.Notification
import com.invictoprojects.model.NotificationType
import reactor.core.publisher.Flux
import java.time.LocalDateTime

interface NotificationService {

    fun getNotifications(username: String, since: LocalDateTime?): Flux<Notification>

    fun addNotificationForUser(username: String, message: String, type: NotificationType, event: Event?): Notification

}
