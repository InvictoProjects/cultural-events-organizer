package com.invictoprojects.repository

import com.invictoprojects.model.EventFeedback
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface EventFeedbackRepository : CrudRepository<EventFeedback, Long> {

    fun findByEventIdAndFeedbackIsNotNullAndFeedbackIsNotEmpty(id: Long): List<EventFeedback>

    fun countByEventIdAndFeedbackIsNotNullAndFeedbackIsNotEmpty(id: Long): Long

    fun getAvgRateByEventId(id: Long): Float

}
