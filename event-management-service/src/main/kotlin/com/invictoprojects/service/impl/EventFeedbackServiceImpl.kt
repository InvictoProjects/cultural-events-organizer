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

    override fun findByEventIdWithNonEmptyFeedback(id: Long): List<EventFeedback> {
        return eventFeedbackRepository.findByEventIdAndFeedbackIsNotNullAndFeedbackIsNotEmpty(id)
    }

    override fun countEventIdWithNonEmptyFeedback(id: Long): Long {
        return eventFeedbackRepository.countByEventIdAndFeedbackIsNotNullAndFeedbackIsNotEmpty(id)
    }

    override fun getAvgRateByEventId(id: Long): Float {
        return eventFeedbackRepository.getAvgRateByEventId(id)
    }

}

