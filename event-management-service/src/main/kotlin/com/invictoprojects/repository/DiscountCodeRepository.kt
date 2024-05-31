package com.invictoprojects.repository

import com.invictoprojects.model.DiscountCode
import com.invictoprojects.model.Event
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface DiscountCodeRepository : CrudRepository<DiscountCode, Long> {
    fun findByCode(code: String): DiscountCode?
    fun findByEvent(event: Event): List<DiscountCode>
}
