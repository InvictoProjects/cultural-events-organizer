package com.invictoprojects.repository

import com.invictoprojects.model.RefreshToken
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository


@Repository
interface RefreshTokenRepository : CrudRepository<RefreshToken, Long> {

    fun findByToken(token: String): RefreshToken?

}
