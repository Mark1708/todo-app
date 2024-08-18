package com.mark1708.todo.domain

import java.util.*

data class Tag(
    val id: UUID = UUID.randomUUID(),
    val name: String
)
