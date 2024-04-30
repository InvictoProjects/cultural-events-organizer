package com.invictoprojects.service.impl

import com.invictoprojects.repository.UserRepository
import com.invictoprojects.service.UserService
import jakarta.inject.Inject
import jakarta.inject.Singleton


@Singleton
class UserServiceImpl (
    @Inject private val userRepository: UserRepository
) : UserService {

    override fun create(user: com.invictoprojects.model.User) {
        val username = user.username
        if (userRepository.existsByUsername(username)) {
            throw IllegalArgumentException("User with email $username already exists")
        }
        userRepository.save(user)
    }

    override fun update(user: com.invictoprojects.model.User) {
        userRepository.update(user)
    }

    override fun findByUsername(username: String): com.invictoprojects.model.User? {
        return userRepository.findByUsername(username)
    }

}
