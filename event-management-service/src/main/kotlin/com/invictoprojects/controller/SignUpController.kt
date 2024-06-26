package com.invictoprojects.controller

import com.invictoprojects.dto.UserDto
import com.invictoprojects.service.AuthenticationService
import com.invictoprojects.utils.MappingUtils
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import jakarta.inject.Inject


@Controller("/api/signup")
@Secured(SecurityRule.IS_ANONYMOUS)
class SignUpController (@Inject private val authenticationService: AuthenticationService) {

    @Post
    fun signUp(@Body userDto: UserDto): HttpResponse<Any> {
        val user = MappingUtils.convertToEntity(userDto)
        authenticationService.signUp(user)
        return HttpResponse.created("Successfully signed up!")
    }

}
