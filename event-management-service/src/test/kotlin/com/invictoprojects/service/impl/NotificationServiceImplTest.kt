package com.invictoprojects.service.impl

import com.invictoprojects.model.*
import com.invictoprojects.repository.NotificationRepository
import com.invictoprojects.service.UserService
import com.invictoprojects.utils.createEvent
import com.invictoprojects.utils.createUser
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class NotificationServiceImplTest {

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var notificationRepository: NotificationRepository

    @InjectMocks
    private lateinit var notificationService: NotificationServiceImpl

    private lateinit var user: User
    private lateinit var notification: Notification
    private lateinit var event: Event

    @BeforeEach
    fun setUp() {
        user = createUser(Role.USER)
        event = createEvent(user)
        notification = Notification(
            message = "Test notification",
            created = LocalDateTime.now(),
            user = user,
            event = event,
            type = NotificationType.EVENT_UPDATE
        )
    }

    @Test
    fun `addNotificationForUser should create and return notification`() {
        whenever(userService.findByUsername(user.username)).thenReturn(user)
        whenever(notificationRepository.save(any())).thenReturn(notification)

        notificationService.addNotificationForUser(user.username, "Test notification", NotificationType.EVENT_UPDATE, event)

        verify(notificationRepository).save(any())
    }

    @Test
    fun `addNotificationForUser should throw exception for non-existing user`() {
        whenever(userService.findByUsername(user.username)).thenReturn(null)

        assertThrows<IllegalArgumentException> {
            notificationService.addNotificationForUser(user.username, "Test notification", NotificationType.EVENT_UPDATE, event)
        }
    }
}
