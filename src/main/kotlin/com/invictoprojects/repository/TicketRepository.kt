package com.invictoprojects.repository

import com.invictoprojects.model.Event
import com.invictoprojects.model.Ticket
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository


@Repository
interface TicketRepository : CrudRepository<Ticket, Long> {

    fun findByUserId(userId: Long): MutableIterable<Ticket>

    fun findByEvent(event: Event): List<Ticket>

}
