package com.invictoprojects.service.impl

import com.invictoprojects.model.DiscountCode
import com.invictoprojects.model.Event
import com.invictoprojects.repository.DiscountCodeRepository
import com.invictoprojects.service.DiscountCodeService
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class DiscountCodeServiceImpl(
    @Inject private val discountCodeRepository: DiscountCodeRepository
) : DiscountCodeService {

    override fun createDiscountCode(event: Event, code: String, discountPercentage: Float): DiscountCode {
        val discountCode = DiscountCode(code, discountPercentage, event)
        return discountCodeRepository.save(discountCode)
    }

    override fun deleteDiscountCode(id: Long) {
        discountCodeRepository.deleteById(id)
    }

    override fun findByCode(code: String): DiscountCode? {
        return discountCodeRepository.findByCode(code)
    }

    override fun findByEvent(event: Event): List<DiscountCode> {
        return discountCodeRepository.findByEvent(event)
    }

}
