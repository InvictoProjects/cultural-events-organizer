package com.invictoprojects.repository

import com.invictoprojects.model.Event
import com.invictoprojects.model.User
import com.invictoprojects.model.WaitList
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface WaitListRepository : CrudRepository<WaitList, Long> {

    fun deleteByEventAndUser(event: Event, user: User)

    fun findByEvent(event: Event): List<WaitList>

}