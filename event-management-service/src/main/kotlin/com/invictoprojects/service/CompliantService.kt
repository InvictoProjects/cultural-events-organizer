package com.invictoprojects.service

import com.invictoprojects.model.Complaint

interface CompliantService {

    fun create(complaint: Complaint): Complaint

    fun getUserComplaints(username: String): List<Complaint>

    fun getEventComplaints(id: Long): List<Complaint>

    fun findById(id: Long): Complaint?

    fun findAll(): List<Complaint>

    fun deleteById(id: Long)

    fun approve(id: Long)

    fun cancel(id: Long)

}
