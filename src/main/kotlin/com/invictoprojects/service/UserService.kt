package com.invictoprojects.service

import com.invictoprojects.model.User


interface UserService {

    fun create(user: User)

    fun findByUsername(username: String): User?

}
