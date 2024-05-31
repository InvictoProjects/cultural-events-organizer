package com.invictoprojects.service

import com.invictoprojects.model.Event
import com.invictoprojects.model.User
import com.invictoprojects.model.VolunteerApplication

interface VolunteerApplicationService {

    fun create(volunteerApplication: VolunteerApplication)

    fun update(volunteerApplication: VolunteerApplication)

    fun deleteByUserAndEvent(event: Event, user: User)

    fun findByEvent(event: Event): List<VolunteerApplication>

    fun findById(id: Long): VolunteerApplication?

}
