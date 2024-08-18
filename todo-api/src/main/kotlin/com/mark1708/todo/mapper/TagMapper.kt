package com.mark1708.todo.mapper

import com.mark1708.todo.domain.Tag
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
interface TagMapper {

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    fun toEntity(name: String): Tag
}