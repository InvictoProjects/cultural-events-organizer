package com.invictoprojects.repository

import com.invictoprojects.model.Complaint
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface ComplaintRepository : CrudRepository<Complaint, Long> {

    fun findByAuthorUsername(username: String): List<Complaint>

    fun findByEventId(id: Long): List<Complaint>

}
