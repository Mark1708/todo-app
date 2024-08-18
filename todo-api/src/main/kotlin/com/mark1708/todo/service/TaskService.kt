package com.mark1708.todo.service

import com.mark1708.todo.dto.CreateTaskDto
import com.mark1708.todo.dto.TaskDto
import com.mark1708.todo.dto.UpdateTaskDto
import com.mark1708.todo.mapper.TagMapper
import com.mark1708.todo.mapper.TaskMapper
import com.mark1708.todo.repository.TaskRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val taskMapper: TaskMapper,
    private val tagMapper: TagMapper,
) {

    fun getAllTasks(): List<TaskDto> {
        return taskRepository.findAll()
            .map(taskMapper::toDto)
    }

    fun getTaskById(id: UUID): TaskDto? {
        val task = taskRepository.findById(id)
        return task?.let(taskMapper::toDto)
    }

    fun createTask(request: CreateTaskDto): TaskDto {
        val task = taskMapper.toEntity(request)
        val tags = request.tags?.map(tagMapper::toEntity)

        val createdTask = taskRepository.create(task, tags)
        return taskMapper.toDto(createdTask)
    }

    fun updateTask(id: UUID, request: UpdateTaskDto): TaskDto? {
        val existingTask = taskRepository.findById(id)
            ?: throw RuntimeException("Task with id $id does not exist")

        val updatedTask = existingTask.copy(
            title = request.title ?: existingTask.title,
            description = request.description ?: existingTask.description,
            done = request.done ?: existingTask.done,
            updatedAt = java.time.LocalDateTime.now()
        )

        val tags = request.tags?.map(tagMapper::toEntity)
        val task = taskRepository.update(updatedTask, tags)

        return taskMapper.toDto(task)
    }

    fun deleteTask(id: UUID) {
        taskRepository.delete(id)
    }
}
