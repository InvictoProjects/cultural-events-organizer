package com.invictoprojects.service.impl

import com.invictoprojects.model.Event
import com.invictoprojects.model.User
import com.invictoprojects.model.VolunteerApplication
import com.invictoprojects.repository.VolunteerApplicationRepository
import com.invictoprojects.service.VolunteerApplicationService
import jakarta.inject.Inject
import jakarta.inject.Singleton


@Singleton
class VolunteerApplicationServiceImpl(
    @Inject private val volunteerApplicationRepository: VolunteerApplicationRepository
) : VolunteerApplicationService {
    override fun create(volunteerApplication: VolunteerApplication) {
        volunteerApplicationRepository.save(volunteerApplication)
    }

    override fun update(volunteerApplication: VolunteerApplication) {
        volunteerApplicationRepository.update(volunteerApplication)
    }

    override fun deleteByUserAndEvent(event: Event, user: User) {
        volunteerApplicationRepository.deleteByEventAndUser(event, user)
    }

    override fun findByEvent(event: Event): List<VolunteerApplication> {
        return volunteerApplicationRepository.findByEvent(event)
    }

    override fun findById(id: Long): VolunteerApplication? {
        return volunteerApplicationRepository.findById(id)
            .orElse(null)
    }

}
