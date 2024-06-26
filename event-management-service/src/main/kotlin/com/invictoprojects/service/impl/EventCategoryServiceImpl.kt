package com.invictoprojects.service.impl

import com.invictoprojects.model.EventCategory
import com.invictoprojects.repository.EventCategoryRepository
import com.invictoprojects.service.EventCategoryService
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class EventCategoryServiceImpl(
        @Inject private val eventCategoryRepository: EventCategoryRepository
) : EventCategoryService {

    override fun create(eventCategory: EventCategory) {
        eventCategoryRepository.save(eventCategory)
    }

    override fun update(eventCategory: EventCategory) {
        eventCategoryRepository.update(eventCategory)
    }

    override fun existsById(id: Long): Boolean {
        return eventCategoryRepository.existsById(id)
    }

    override fun findByName(name: String): EventCategory? {
        return eventCategoryRepository.findByName(name)
    }

    override fun deleteById(id: Long) {
        eventCategoryRepository.deleteById(id)
    }
}
