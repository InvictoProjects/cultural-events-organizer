package com.invictoprojects.service.impl

import com.invictoprojects.model.User
import com.invictoprojects.model.Role
import com.invictoprojects.repository.UserRepository
import com.invictoprojects.utils.createUser
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class UserServiceImplTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var userService: UserServiceImpl

    private lateinit var user: User

    @BeforeEach
    fun setUp() {
        user = createUser(Role.USER)
    }

    @Test
    fun `create should save user if username does not exist`() {
        whenever(userRepository.existsByUsername(user.username)).thenReturn(false)

        userService.create(user)

        verify(userRepository).save(user)
    }

    @Test
    fun `create should throw exception if username already exists`() {
        whenever(userRepository.existsByUsername(user.username)).thenReturn(true)

        val exception = assertThrows<IllegalArgumentException> {
            userService.create(user)
        }

        assertEquals("User with email ${user.username} already exists", exception.message)
        verify(userRepository).existsByUsername(user.username)
    }

    @Test
    fun `update should update user`() {
        userService.update(user)

        verify(userRepository).update(user)
    }

    @Test
    fun `findByUsername should return user if found`() {
        whenever(userRepository.findByUsername(user.username)).thenReturn(user)

        val result = userService.findByUsername(user.username)

        assertEquals(user, result)
    }

    @Test
    fun `findByUsername should return null if not found`() {
        whenever(userRepository.findByUsername(user.username)).thenReturn(null)

        val result = userService.findByUsername(user.username)

        assertNull(result)
    }
}
