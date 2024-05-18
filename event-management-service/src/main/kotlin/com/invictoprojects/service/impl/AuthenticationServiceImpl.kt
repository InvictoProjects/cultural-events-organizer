package com.invictoprojects.service.impl

import com.invictoprojects.model.Role
import com.invictoprojects.model.User
import com.invictoprojects.security.CustomPasswordEncoder
import com.invictoprojects.service.AuthenticationService
import com.invictoprojects.service.UserService
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class AuthenticationServiceImpl(
    @Inject private val userService: UserService,
    @Inject private val customPasswordEncoder: CustomPasswordEncoder
) : AuthenticationService {

    override fun signUp(user: User) {
        user.passwordHash = customPasswordEncoder.encode(user.passwordHash)
        user.role = Role.USER
        userService.create(user)
    }

}
