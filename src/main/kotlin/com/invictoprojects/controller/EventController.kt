package com.invictoprojects.controller

import com.invictoprojects.model.Event
import com.invictoprojects.repository.EventRepository
import io.micronaut.http.annotation.*
import jakarta.inject.Inject

@Controller("/events")
class EventController(@Inject private val repository: EventRepository) {

    @Post
    fun create(@Body event: Event): Event = repository.create(event)

    @Get("/{id}")
    fun findById(id: Long): Event? = repository.findById(id)

    @Get
    fun findAll(): List<Event> = repository.findAll()

    @Put
    fun update(@Body event: Event): Event = repository.update(event)

    @Delete("/{id}")
    fun deleteById(id: Long): Boolean = repository.deleteById(id)
}