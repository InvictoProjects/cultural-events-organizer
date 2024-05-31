package com.invictoprojects.service.impl

import com.invictoprojects.model.EventMedia
import com.invictoprojects.repository.EventMediaRepository
import com.invictoprojects.service.EventMediaService
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class EventMediaServiceImpl(
    @Inject private val eventMediaRepository: EventMediaRepository
) : EventMediaService {

    override fun addMediaToEvent(media: EventMedia) {
        eventMediaRepository.save(media)
    }

    override fun findByEventId(id: Long): List<EventMedia> {
        return eventMediaRepository.findByEventId(id)
    }

    override fun findById(id: Long): EventMedia {
        return eventMediaRepository.findById(id).get()
    }

    override fun deleteById(id: Long) {
        eventMediaRepository.deleteById(id)
    }
}
