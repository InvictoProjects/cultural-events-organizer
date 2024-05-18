package com.invictoprojects.service

import com.invictoprojects.model.Event
import com.invictoprojects.model.Ticket
import com.invictoprojects.model.User

interface TicketService {

    fun purchaseTickets(event: Event, user: User, amount: Long)

    fun findPurchasedTicketsByUserId(userId: Long) : MutableIterable<Ticket>

    fun deleteById(id: Long)

}
