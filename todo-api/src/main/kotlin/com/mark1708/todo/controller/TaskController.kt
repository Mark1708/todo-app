package com.mark1708.todo.controller

import com.mark1708.todo.dto.CreateTaskDto
import com.mark1708.todo.dto.TaskDto
import com.mark1708.todo.dto.UpdateTaskDto
import com.mark1708.todo.service.TaskService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/v1/tasks")
class TaskController(private val taskService: TaskService) {

    @GetMapping
    fun getAllTasks(): List<TaskDto> =
        taskService.getAllTasks()

    @GetMapping("/{id}")
    fun getTaskById(@PathVariable id: UUID): TaskDto? =
        taskService.getTaskById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createTask(@RequestBody request: CreateTaskDto): TaskDto =
        taskService.createTask(request)

    @PutMapping("/{id}")
    fun updateTask(@PathVariable id: UUID, @RequestBody request: UpdateTaskDto): TaskDto? =
        taskService.updateTask(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTask(@PathVariable id: UUID) =
        taskService.deleteTask(id)
}
