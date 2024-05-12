package com.invictoprojects.service

import com.invictoprojects.model.Event
import com.invictoprojects.model.User
import com.invictoprojects.model.WaitList

interface WaitListService {

    fun create(event: Event, user: User): WaitList

    fun deleteByEventAndUser(event: Event, user: User)

    fun findByEvent(event: Event): List<WaitList>

}