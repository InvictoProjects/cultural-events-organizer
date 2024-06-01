package com.invictoprojects.utils

import com.invictoprojects.model.*
import java.time.LocalDateTime

fun createUser(role: Role): User {
    return User(
        username = "testuser",
        passwordHash = "plaintextpassword",
        email = "testuser@example.com",
        firstName = "Test",
        lastName = "User",
        role = role,
        blocked = false,
        id = 1L
    )
}

fun createEvent(user: User): Event {
    return Event(
        name = "Test Event",
        description = "Event Description",
        startTime = LocalDateTime.now().minusHours(3),
        endTime = LocalDateTime.now().minusHours(2),
        eventType = EventType.ONLINE,
        category = EventCategory("Category", null),
        creators = mutableSetOf(user),
        location = "Location",
        isTicketsLimited = false,
        maxTickets = null,
        isAskingForVolunteers = false,
        maxVolunteersAmount = null,
        volunteers = mutableSetOf(),
        price = 100.0,
        isBlocked = false,
        isPrivate = false,
        invitationCode = null,
        url = "http://example.com",
        id = 1L
    )
}

fun createEvent(user: User, startTime: LocalDateTime, endTime: LocalDateTime, isTicketLimited: Boolean): Event {
    return Event(
        name = "Test Event",
        description = "Event Description",
        startTime = startTime,
        endTime = endTime,
        eventType = EventType.ONLINE,
        category = EventCategory("Category", null),
        creators = mutableSetOf(user),
        location = "Location",
        isTicketsLimited = isTicketLimited,
        maxTickets = 100,
        isAskingForVolunteers = false,
        maxVolunteersAmount = null,
        volunteers = mutableSetOf(),
        price = 100.0,
        isBlocked = false,
        isPrivate = false,
        invitationCode = null,
        url = "http://example.com",
        id = 1L
    )
}
