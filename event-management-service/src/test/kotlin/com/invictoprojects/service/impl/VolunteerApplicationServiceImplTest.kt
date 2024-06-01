package com.invictoprojects.service.impl

import com.invictoprojects.model.*
import com.invictoprojects.repository.VolunteerApplicationRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class VolunteerApplicationServiceImplTest {

    @Mock
    private lateinit var volunteerApplicationRepository: VolunteerApplicationRepository

    @InjectMocks
    private lateinit var volunteerApplicationService: VolunteerApplicationServiceImpl

    private lateinit var event: Event
    private lateinit var user: User
    private lateinit var volunteerApplication: VolunteerApplication

    @BeforeEach
    fun setUp() {
        user = User(
            username = "testuser",
            passwordHash = "plaintextpassword",
            email = "testuser@example.com",
            firstName = "Test",
            lastName = "User",
            role = Role.USER,
            blocked = false,
            id = 1L
        )
        event = Event(
            name = "Test Event",
            description = "Event Description",
            startTime = LocalDateTime.now().plusDays(1),
            endTime = LocalDateTime.now().plusDays(2),
            eventType = EventType.ONLINE,
            category = EventCategory("Category", null),
            creators = mutableSetOf(user),
            location = "Location",
            isTicketsLimited = false,
            maxTickets = null,
            isAskingForVolunteers = false,
            maxVolunteersAmount = null,
            volunteers = mutableSetOf(),
            price = 100.0,
            isBlocked = false,
            isPrivate = false,
            invitationCode = null,
            url = "http://example.com",
            id = 1L
        )
        volunteerApplication = VolunteerApplication(
            user = user,
            event = event,
            status = VolunteerApplicationStatus.PENDING
        )
    }

    @Test
    fun `create should save volunteer application`() {
        volunteerApplicationService.create(volunteerApplication)

        verify(volunteerApplicationRepository).save(volunteerApplication)
    }

    @Test
    fun `update should update volunteer application`() {
        volunteerApplicationService.update(volunteerApplication)

        verify(volunteerApplicationRepository).update(volunteerApplication)
    }

    @Test
    fun `deleteByUserAndEvent should delete volunteer application`() {
        volunteerApplicationService.deleteByUserAndEvent(event, user)

        verify(volunteerApplicationRepository).deleteByEventAndUser(event, user)
    }

    @Test
    fun `findByEvent should return volunteer applications for event`() {
        val applications = listOf(volunteerApplication)
        whenever(volunteerApplicationRepository.findByEvent(event)).thenReturn(applications)

        val result = volunteerApplicationService.findByEvent(event)

        assertEquals(applications, result)
    }

    @Test
    fun `findById should return volunteer application if found`() {
        volunteerApplication.id = 123L
        whenever(volunteerApplicationRepository.findById(volunteerApplication.id!!)).thenReturn(Optional.of(volunteerApplication))

        val result = volunteerApplicationService.findById(volunteerApplication.id!!)

        assertEquals(volunteerApplication, result)
    }

    @Test
    fun `findById should return null if not found`() {
        whenever(volunteerApplicationRepository.findById(any())).thenReturn(Optional.empty())

        val result = volunteerApplicationService.findById(1L)

        assertNull(result)
    }
}
