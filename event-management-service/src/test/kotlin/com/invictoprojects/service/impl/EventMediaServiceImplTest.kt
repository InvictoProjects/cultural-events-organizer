package com.invictoprojects.service.impl

import com.invictoprojects.model.Event
import com.invictoprojects.model.EventMedia
import com.invictoprojects.model.Role
import com.invictoprojects.repository.EventMediaRepository
import com.invictoprojects.utils.createEvent
import com.invictoprojects.utils.createUser
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.*

@ExtendWith(MockitoExtension::class)
class EventMediaServiceImplTest {

    @Mock
    private lateinit var eventMediaRepository: EventMediaRepository

    @InjectMocks
    private lateinit var eventMediaService: EventMediaServiceImpl

    private lateinit var eventMedia: EventMedia
    private lateinit var event: Event

    @BeforeEach
    fun setUp() {
        val user = createUser(Role.USER)
        event = createEvent(user)
        eventMedia = EventMedia(
            s3Key = "s3://bucket/key",
            event = event,
            id = 1L
        )
    }

    @Test
    fun `addMediaToEvent should save media`() {
        eventMediaService.addMediaToEvent(eventMedia)

        verify(eventMediaRepository).save(eventMedia)
    }

    @Test
    fun `findByEventId should return media for the event`() {
        val mediaList = listOf(eventMedia)
        whenever(eventMediaRepository.findByEventId(event.id!!)).thenReturn(mediaList)

        val result = eventMediaService.findByEventId(event.id!!)

        assertEquals(mediaList, result)
        verify(eventMediaRepository).findByEventId(event.id!!)
    }

    @Test
    fun `findById should return media if found`() {
        whenever(eventMediaRepository.findById(eventMedia.id!!)).thenReturn(Optional.of(eventMedia))

        val result = eventMediaService.findById(eventMedia.id!!)

        assertEquals(eventMedia, result)
        verify(eventMediaRepository).findById(eventMedia.id!!)
    }

    @Test
    fun `findById should throw exception if not found`() {
        whenever(eventMediaRepository.findById(any())).thenReturn(Optional.empty())

        assertThrows<NoSuchElementException> {
            eventMediaService.findById(1L)
        }
        verify(eventMediaRepository).findById(1L)
    }

    @Test
    fun `deleteById should delete media`() {
        eventMediaService.deleteById(1L)

        verify(eventMediaRepository).deleteById(1L)
    }
}
