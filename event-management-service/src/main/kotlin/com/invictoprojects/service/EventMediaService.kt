package com.invictoprojects.service

import com.invictoprojects.model.EventMedia

interface EventMediaService {

    fun addMediaToEvent(media: EventMedia)

    fun findByEventId(id: Long): List<EventMedia>

    fun findById(id: Long): EventMedia

    fun deleteById(id: Long)

}