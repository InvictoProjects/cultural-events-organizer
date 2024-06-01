package com.invictoprojects.service.impl

import com.invictoprojects.model.DiscountCode
import com.invictoprojects.model.Event
import com.invictoprojects.model.Role
import com.invictoprojects.repository.DiscountCodeRepository
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
class DiscountCodeServiceImplTest {

    @Mock
    private lateinit var discountCodeRepository: DiscountCodeRepository

    @InjectMocks
    private lateinit var discountCodeService: DiscountCodeServiceImpl

    private lateinit var event: Event
    private lateinit var discountCode: DiscountCode

    @BeforeEach
    fun setUp() {
        val user = createUser(Role.USER)
        event = createEvent(user)
        discountCode = DiscountCode(
            code = "DISCOUNT10",
            discountPercentage = 10.0f,
            event = event,
            id = 1L
        )
    }

    @Test
    fun `createDiscountCode should save and return discount code`() {
        whenever(discountCodeRepository.save(any())).thenReturn(discountCode)

        val result = discountCodeService.createDiscountCode(event, "DISCOUNT10", 10.0f)

        assertEquals(discountCode, result)
        verify(discountCodeRepository).save(any())
    }

    @Test
    fun `deleteDiscountCode should delete discount code by id`() {
        discountCodeService.deleteDiscountCode(1L)

        verify(discountCodeRepository).deleteById(1L)
    }

    @Test
    fun `findByCode should return discount code if found`() {
        whenever(discountCodeRepository.findByCode("DISCOUNT10")).thenReturn(discountCode)

        val result = discountCodeService.findByCode("DISCOUNT10")

        assertEquals(discountCode, result)
        verify(discountCodeRepository).findByCode("DISCOUNT10")
    }

    @Test
    fun `findByCode should return null if not found`() {
        whenever(discountCodeRepository.findByCode("DISCOUNT10")).thenReturn(null)

        val result = discountCodeService.findByCode("DISCOUNT10")

        assertNull(result)
        verify(discountCodeRepository).findByCode("DISCOUNT10")
    }

    @Test
    fun `findByEvent should return list of discount codes`() {
        val discountCodes = listOf(discountCode)
        whenever(discountCodeRepository.findByEvent(event)).thenReturn(discountCodes)

        val result = discountCodeService.findByEvent(event)

        assertEquals(discountCodes, result)
        verify(discountCodeRepository).findByEvent(event)
    }
}
