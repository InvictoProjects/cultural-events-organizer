package com.invictoprojects.service

import com.invictoprojects.model.User


interface AuthenticationService {

    fun signUp(user: User)

}
