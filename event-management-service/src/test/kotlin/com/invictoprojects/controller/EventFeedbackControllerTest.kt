package com.invictoprojects.controller

import com.invictoprojects.model.Role
import com.invictoprojects.service.EventFeedbackService
import com.invictoprojects.service.EventService
import com.invictoprojects.service.TicketService
import com.invictoprojects.service.UserService
import com.invictoprojects.utils.createEvent
import com.invictoprojects.utils.createUser
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import java.security.Principal

@ExtendWith(MockitoExtension::class)
class EventFeedbackControllerTest {

    @Mock
    lateinit var eventService: EventService

    @Mock
    lateinit var ticketService: TicketService

    @Mock
    lateinit var userService: UserService

    @Mock
    lateinit var eventFeedbackService: EventFeedbackService

    @InjectMocks
    lateinit var eventFeedbackController: EventFeedbackController

    private lateinit var principal: Principal

    @BeforeEach
    fun setup() {
        principal = mock(Principal::class.java)
    }

    @Test
    fun `getEventFeedbacks should return OK when feedbacks are retrieved successfully`() {
        val user = createUser(Role.USER)
        val event = createEvent(user)

        whenever(userService.findByUsername(user.username)).thenReturn(user)
        whenever(eventService.findById(event.id!!)).thenReturn(event)
        whenever(eventFeedbackService.findByEventIdWithNonEmptyFeedback(event.id!!)).thenReturn(emptyList())
        whenever(principal.name).thenReturn(user.username)

        val response: HttpResponse<Any> = eventFeedbackController.getEventFeedbacks(event.id!!, principal)

        assertEquals(HttpStatus.OK, response.status)
    }

}
