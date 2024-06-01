package com.invictoprojects.service.impl

import com.invictoprojects.model.EventCategory
import com.invictoprojects.repository.EventCategoryRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class EventCategoryServiceImplTest {

    @Mock
    private lateinit var eventCategoryRepository: EventCategoryRepository

    @InjectMocks
    private lateinit var eventCategoryService: EventCategoryServiceImpl

    private lateinit var eventCategory: EventCategory

    @BeforeEach
    fun setUp() {
        eventCategory = EventCategory(
            name = "Music",
            description = "Music events",
            id = 1L
        )
    }

    @Test
    fun `create should save event category`() {
        eventCategoryService.create(eventCategory)

        verify(eventCategoryRepository).save(eventCategory)
    }

    @Test
    fun `update should update event category`() {
        eventCategoryService.update(eventCategory)

        verify(eventCategoryRepository).update(eventCategory)
    }

    @Test
    fun `existsById should return true if category exists`() {
        whenever(eventCategoryRepository.existsById(1L)).thenReturn(true)

        val result = eventCategoryService.existsById(1L)

        assertTrue(result)
        verify(eventCategoryRepository).existsById(1L)
    }

    @Test
    fun `existsById should return false if category does not exist`() {
        whenever(eventCategoryRepository.existsById(1L)).thenReturn(false)

        val result = eventCategoryService.existsById(1L)

        assertFalse(result)
        verify(eventCategoryRepository).existsById(1L)
    }

    @Test
    fun `findByName should return event category if found`() {
        whenever(eventCategoryRepository.findByName("Music")).thenReturn(eventCategory)

        val result = eventCategoryService.findByName("Music")

        assertEquals(eventCategory, result)
        verify(eventCategoryRepository).findByName("Music")
    }

    @Test
    fun `findByName should return null if not found`() {
        whenever(eventCategoryRepository.findByName("Music")).thenReturn(null)

        val result = eventCategoryService.findByName("Music")

        assertNull(result)
        verify(eventCategoryRepository).findByName("Music")
    }

    @Test
    fun `deleteById should delete event category`() {
        eventCategoryService.deleteById(1L)

        verify(eventCategoryRepository).deleteById(1L)
    }
}
