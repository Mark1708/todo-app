package com.mark1708.todo.dto

data class CreateTaskDto(
    val title: String,
    val description: String?,
    val tags: List<String>?
)
