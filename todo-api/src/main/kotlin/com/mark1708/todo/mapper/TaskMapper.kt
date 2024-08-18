package com.mark1708.todo.mapper

import com.mark1708.todo.domain.Task
import com.mark1708.todo.dto.CreateTaskDto
import com.mark1708.todo.dto.TaskDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
interface TaskMapper {

    @Mapping(target = "tags", expression = "java(task.getTags() != null ? task.getTags().stream().map(tag -> tag.getName()).collect(java.util.stream.Collectors.toList()) : java.util.Collections.emptyList())")
    fun toDto(task: Task): TaskDto

    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "done", expression = "java(false)")
    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    fun toEntity(task: CreateTaskDto): Task
}
