package com.invictoprojects.service

import com.invictoprojects.model.DiscountCode
import com.invictoprojects.model.Event

interface DiscountCodeService {

    fun createDiscountCode(event: Event, code: String, discountPercentage: Float): DiscountCode

    fun deleteDiscountCode(id: Long)

    fun findByCode(code: String): DiscountCode?

    fun findByEvent(event: Event): List<DiscountCode>

}
