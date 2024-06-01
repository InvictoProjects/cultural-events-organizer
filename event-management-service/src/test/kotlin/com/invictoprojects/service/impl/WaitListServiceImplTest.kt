package com.invictoprojects.service.impl

import com.invictoprojects.model.Event
import com.invictoprojects.model.Role
import com.invictoprojects.model.User
import com.invictoprojects.model.WaitList
import com.invictoprojects.repository.WaitListRepository
import com.invictoprojects.service.WaitListService
import com.invictoprojects.utils.createEvent
import com.invictoprojects.utils.createUser
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

@ExtendWith(MockitoExtension::class)
class WaitListServiceImplTest {

    @Mock
    private lateinit var waitListRepository: WaitListRepository

    @InjectMocks
    private lateinit var waitListService: WaitListServiceImpl

    private lateinit var event: Event
    private lateinit var user: User
    private lateinit var waitList: WaitList

    @BeforeEach
    fun setUp() {
        user = createUser(Role.USER)
        event = createEvent(user)
        waitList = WaitList(
            event = event,
            user = user,
            id = 1L
        )
    }

    @Test
    fun `create should save waitlist entry`() {
        whenever(waitListRepository.save(any())).thenReturn(waitList)

        val result = waitListService.create(event, user)

        assertEquals(waitList, result)
        verify(waitListRepository).save(any())
    }

    @Test
    fun `deleteByEventAndUser should delete waitlist entry`() {
        waitListService.deleteByEventAndUser(event, user)

        verify(waitListRepository).deleteByEventAndUser(event, user)
    }

    @Test
    fun `findByEvent should return waitlist entries for event`() {
        val waitListEntries = listOf(waitList)
        whenever(waitListRepository.findByEvent(event)).thenReturn(waitListEntries)

        val result = waitListService.findByEvent(event)

        assertEquals(waitListEntries, result)
    }
}
