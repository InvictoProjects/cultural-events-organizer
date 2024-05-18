package com.invictoprojects.service

import com.invictoprojects.model.EventFeedback

interface EventFeedbackService {

    fun leaveFeedback(eventFeedback: EventFeedback)

}
