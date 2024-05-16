package com.invictoprojects.repository

import com.invictoprojects.model.EventMedia
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface EventMediaRepository : CrudRepository<EventMedia, Long> {

    fun findByEventId(id: Long): List<EventMedia>

}
