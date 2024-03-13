package com.invictoprojects.security

import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory


@Factory
class PasswordEncoderConfiguration {
    @Bean
    fun customPasswordEncoder(): CustomPasswordEncoder {
        return CustomPasswordEncoder()
    }
}
