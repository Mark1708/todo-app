package com.mark1708.todo.domain

import java.time.LocalDateTime
import java.util.*

data class Task(
    val id: UUID = UUID.randomUUID(),

    val title: String,
    val description: String?,

    var done: Boolean = false,

    val createdAt: LocalDateTime,
    var updatedAt: LocalDateTime,

    val tags: Set<Tag>? = emptySet()
)
