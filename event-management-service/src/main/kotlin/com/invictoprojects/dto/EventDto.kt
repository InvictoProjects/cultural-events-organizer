package com.invictoprojects.dto

import com.invictoprojects.model.EventType
import io.micronaut.core.annotation.Introspected
import java.time.LocalDateTime
import javax.validation.constraints.Future
import javax.validation.constraints.NotBlank


@Introspected
data class EventDto(
    @NotBlank
    val name: String,

    @NotBlank
    val description: String,

    @Future
    val startTime: LocalDateTime,

    @Future
    val endTime: LocalDateTime,

    @NotBlank
    val eventType: EventType,

    @NotBlank
    val category: String,

    val location: String?,

    val isTicketsLimited: Boolean,

    val maxTickets: Long? = null,

    val url: String?
)
