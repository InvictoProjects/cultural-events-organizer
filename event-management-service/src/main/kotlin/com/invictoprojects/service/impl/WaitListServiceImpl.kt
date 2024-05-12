package com.invictoprojects.service.impl

import com.invictoprojects.model.Event
import com.invictoprojects.model.User
import com.invictoprojects.model.WaitList
import com.invictoprojects.repository.WaitListRepository
import com.invictoprojects.service.WaitListService
import jakarta.inject.Inject
import jakarta.inject.Singleton


@Singleton
class WaitListServiceImpl(
    @Inject private val waitListRepository: WaitListRepository
) : WaitListService {

    override fun create(event: Event, user: User): WaitList {
        val entity = WaitList(
            event = event,
            user = user
        )
        return waitListRepository.save(entity)
    }

    override fun deleteByEventAndUser(event: Event, user: User) {
        waitListRepository.deleteByEventAndUser(event, user)
    }

    override fun findByEvent(event: Event): List<WaitList> {
        return waitListRepository.findByEvent(event)
    }
}
