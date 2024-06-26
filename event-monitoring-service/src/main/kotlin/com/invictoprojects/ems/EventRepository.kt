package com.invictoprojects.ems

import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import java.time.LocalDateTime

@Repository
interface EventRepository : CrudRepository<Event, Long> {

    @Query("""
        SELECT t.event_id, t.user_id FROM events JOIN tickets t on events.id = t.event_id
         WHERE end_time < now()
    """, nativeQuery = true)
    fun findEndedEvents(): MutableList<Pair<Long, Long>>

    @Query("""
        SELECT t.event_id, t.user_id FROM events JOIN tickets t on events.id = t.event_id
         WHERE start_time BETWEEN now() AND now() + INTERVAL '30 minute'
    """, nativeQuery = true)
    fun findStartingEvents(): List<Pair<Long, Long>>
}