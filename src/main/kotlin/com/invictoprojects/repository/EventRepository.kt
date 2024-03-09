package com.invictoprojects.repository

import com.invictoprojects.model.Event

interface EventRepository {
    fun create(event: Event): Event
    fun findById(id: Long): Event?
    fun findAll(): List<Event>
    fun update(event: Event): Event
    fun deleteById(id: Long): Boolean
}
