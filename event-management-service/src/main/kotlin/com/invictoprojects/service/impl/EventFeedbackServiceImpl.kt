package com.invictoprojects.service.impl

import com.invictoprojects.model.EventFeedback
import com.invictoprojects.repository.EventFeedbackRepository
import com.invictoprojects.service.EventFeedbackService
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class EventFeedbackServiceImpl(
    @Inject private val eventFeedbackRepository: EventFeedbackRepository
) : EventFeedbackService {

    override fun leaveFeedback(eventFeedback: EventFeedback) {
        eventFeedbackRepository.save(eventFeedback)
    }

}
