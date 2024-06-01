package com.invictoprojects.controller

import com.invictoprojects.dto.BlockDto
import com.invictoprojects.model.Role
import com.invictoprojects.model.User
import com.invictoprojects.service.UserService
import io.micronaut.http.HttpStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class AdminControllerTest {

    @Mock
    private lateinit var userService: UserService

    @InjectMocks
    private lateinit var adminController: AdminController

    private val adminUser = User(
        username = "admin",
        passwordHash = "password",
        email = "admin@example.com",
        firstName = "Admin",
        lastName = "User",
        role = Role.ADMIN,
        blocked = false,
        id = 1L
    )

    private val regularUser = User(
        username = "user",
        passwordHash = "password",
        email = "user@example.com",
        firstName = "Regular",
        lastName = "User",
        role = Role.USER,
        blocked = false,
        id = 2L
    )

    @Test
    fun `blockUser should block user if user exists and is not admin`() {
        val blockDto = BlockDto(username = "user")
        whenever(userService.findByUsername("user")).thenReturn(regularUser)

        val response = adminController.blockUser(blockDto)

        assertEquals(HttpStatus.OK, response.status())
        assertEquals(true, regularUser.blocked)
    }

    @Test
    fun `blockUser should return bad request if user is admin`() {
        val blockDto = BlockDto(username = "admin")
        whenever(userService.findByUsername("admin")).thenReturn(adminUser)

        val response = adminController.blockUser(blockDto)

        assertEquals(HttpStatus.BAD_REQUEST, response.status())
        assertEquals("Can not block admin user", response.body())
    }

    @Test
    fun `blockUser should return not found if user does not exist`() {
        val blockDto = BlockDto(username = "unknown")
        whenever(userService.findByUsername("unknown")).thenReturn(null)

        val response = adminController.blockUser(blockDto)

        assertEquals(HttpStatus.NOT_FOUND, response.status())
        assertEquals("User with such a username not found", response.body())
    }

    @Test
    fun `unblockUser should unblock user if user exists and is not admin`() {
        val blockDto = BlockDto(username = "user")
        regularUser.blocked = true
        whenever(userService.findByUsername("user")).thenReturn(regularUser)

        val response = adminController.unblockUser(blockDto)

        assertEquals(HttpStatus.OK, response.status())
        assertEquals(false, regularUser.blocked)
    }

    @Test
    fun `unblockUser should return bad request if user is admin`() {
        val blockDto = BlockDto(username = "admin")
        whenever(userService.findByUsername("admin")).thenReturn(adminUser)

        val response = adminController.unblockUser(blockDto)

        assertEquals(HttpStatus.BAD_REQUEST, response.status())
        assertEquals("Can not unblock admin user", response.body())
    }

    @Test
    fun `unblockUser should return not found if user does not exist`() {
        val blockDto = BlockDto(username = "unknown")
        whenever(userService.findByUsername("unknown")).thenReturn(null)

        val response = adminController.unblockUser(blockDto)

        assertEquals(HttpStatus.NOT_FOUND, response.status())
        assertEquals("User with such a username not found", response.body())
    }
}
