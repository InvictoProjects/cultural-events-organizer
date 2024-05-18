package com.invictoprojects.repository

import com.invictoprojects.model.Notification
import com.invictoprojects.model.User
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import java.time.LocalDateTime

@Repository
interface NotificationRepository : CrudRepository<Notification, Long> {

    fun findByUserAndCreatedAfter(user: User, since: LocalDateTime): List<Notification>

    fun findByUser(user: User): List<Notification>

}
