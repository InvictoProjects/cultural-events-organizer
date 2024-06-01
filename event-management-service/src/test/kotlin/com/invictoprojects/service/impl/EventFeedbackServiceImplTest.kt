package com.invictoprojects.service.impl

import com.invictoprojects.model.Event
import com.invictoprojects.model.EventFeedback
import com.invictoprojects.model.Role
import com.invictoprojects.model.User
import com.invictoprojects.repository.EventFeedbackRepository
import com.invictoprojects.utils.createEvent
import com.invictoprojects.utils.createUser
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class EventFeedbackServiceImplTest {

    @Mock
    private lateinit var eventFeedbackRepository: EventFeedbackRepository

    @InjectMocks
    private lateinit var eventFeedbackService: EventFeedbackServiceImpl

    private lateinit var eventFeedback: EventFeedback
    private lateinit var event: Event
    private lateinit var user: User

    @BeforeEach
    fun setUp() {
        user = createUser(Role.USER)
        event = createEvent(user)
        eventFeedback = EventFeedback(
            user = user,
            event = event,
            rate = 5,
            feedback = "Great event!",
            id = 1L
        )
    }

    @Test
    fun `leaveFeedback should save event feedback`() {
        eventFeedbackService.leaveFeedback(eventFeedback)

        verify(eventFeedbackRepository).save(eventFeedback)
    }

    @Test
    fun `findByEventIdWithNonEmptyFeedback should return feedback with non-empty feedback`() {
        val feedbacks = listOf(eventFeedback)
        whenever(eventFeedbackRepository.findByEventIdAndFeedbackIsNotNullAndFeedbackIsNotEmpty(event.id!!)).thenReturn(feedbacks)

        val result = eventFeedbackService.findByEventIdWithNonEmptyFeedback(event.id!!)

        assertEquals(feedbacks, result)
        verify(eventFeedbackRepository).findByEventIdAndFeedbackIsNotNullAndFeedbackIsNotEmpty(event.id!!)
    }

    @Test
    fun `countEventIdWithNonEmptyFeedback should return count of non-empty feedback`() {
        whenever(eventFeedbackRepository.countByEventIdAndFeedbackIsNotNullAndFeedbackIsNotEmpty(event.id!!)).thenReturn(1L)

        val result = eventFeedbackService.countEventIdWithNonEmptyFeedback(event.id!!)

        assertEquals(1L, result)
        verify(eventFeedbackRepository).countByEventIdAndFeedbackIsNotNullAndFeedbackIsNotEmpty(event.id!!)
    }

    @Test
    fun `getAvgRateByEventId should return average rate`() {
        whenever(eventFeedbackRepository.getAvgRateByEventId(event.id!!)).thenReturn(4.5f)

        val result = eventFeedbackService.getAvgRateByEventId(event.id!!)

        assertEquals(4.5f, result)
        verify(eventFeedbackRepository).getAvgRateByEventId(event.id!!)
    }
}
