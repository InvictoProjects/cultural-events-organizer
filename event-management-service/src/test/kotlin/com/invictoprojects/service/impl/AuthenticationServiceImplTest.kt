package com.invictoprojects.service.impl

import com.invictoprojects.model.Role
import com.invictoprojects.model.User
import com.invictoprojects.security.CustomPasswordEncoder
import com.invictoprojects.service.UserService
import com.invictoprojects.utils.createUser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class AuthenticationServiceImplTest {

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var customPasswordEncoder: CustomPasswordEncoder

    @InjectMocks
    private lateinit var authenticationService: AuthenticationServiceImpl

    private lateinit var user: User

    @BeforeEach
    fun setUp() {
        user = createUser(Role.USER)
    }

    @Test
    fun `signUp should encode password and assign role`() {
        val encodedPassword = "encodedpassword"
        whenever(customPasswordEncoder.encode(any())).thenReturn(encodedPassword)
        doNothing().whenever(userService).create(any())

        authenticationService.signUp(user)

        assertEquals(encodedPassword, user.passwordHash)
        assertEquals(Role.USER, user.role)
        verify(userService).create(user)
    }
}
