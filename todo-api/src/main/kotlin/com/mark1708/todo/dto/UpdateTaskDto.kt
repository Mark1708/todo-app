package com.mark1708.todo.dto

data class UpdateTaskDto(
    val title: String?,
    val description: String?,
    val done: Boolean?,
    val tags: List<String>?
)
