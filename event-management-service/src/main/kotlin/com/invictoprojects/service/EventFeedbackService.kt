package com.invictoprojects.service

import com.invictoprojects.model.EventFeedback

interface EventFeedbackService {

    fun leaveFeedback(eventFeedback: EventFeedback)

    fun findByEventIdWithNonEmptyFeedback(id: Long): List<EventFeedback>

    fun countEventIdWithNonEmptyFeedback(id: Long): Long

    fun getAvgRateByEventId(id: Long): Float


}