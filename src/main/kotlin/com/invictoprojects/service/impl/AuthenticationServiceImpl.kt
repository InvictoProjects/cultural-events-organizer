package com.invictoprojects.service.impl

import com.invictoprojects.model.User
import com.invictoprojects.service.AuthenticationService
import com.invictoprojects.service.UserService
import jakarta.inject.Inject
import jakarta.inject.Singleton


@Singleton
class AuthenticationServiceImpl(
    @Inject private val userService: UserService
) : AuthenticationService {

    override fun signUp(user: User) {
        //TODO: add password encryption
        userService.create(user)
    }

}
