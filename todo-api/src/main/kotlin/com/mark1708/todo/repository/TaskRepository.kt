package com.mark1708.todo.repository

import com.mark1708.todo.domain.Tag
import com.mark1708.todo.domain.Task
import com.mark1708.todo.generated.Tables.*
import com.mark1708.todo.generated.tables.records.TodoTagRecord
import com.mark1708.todo.generated.tables.records.TodoTaskRecord
import org.jooq.DSLContext
import org.jooq.Result
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class TaskRepository(private val dsl: DSLContext) {

    fun findAll(): List<Task> {
        return selectTasks()
            .fetchGroups(
                TODO_TASK,
                TODO_TAG
            ).map { (taskRecord, tags) -> toTask(taskRecord, tags) }
    }

    fun findById(id: UUID): Task? {
        return selectTasks()
            .where(TODO_TASK.ID.eq(id))
            .fetchGroups(
                TODO_TASK,
                TODO_TAG
            )
            .map { (taskRecord, tags) -> toTask(taskRecord, tags) }
            .firstOrNull()
    }

    fun create(task: Task, tags: List<Tag>?): Task {
        dsl.transaction { configuration ->
            val ctx = DSL.using(configuration)
            ctx.insertInto(TODO_TASK)
                .set(TODO_TASK.ID, task.id)
                .set(TODO_TASK.TITLE, task.title)
                .set(TODO_TASK.DESCRIPTION, task.description)
                .set(TODO_TASK.DONE, task.done)
                .set(TODO_TASK.CREATED_AT, task.createdAt)
                .set(TODO_TASK.UPDATED_AT, task.updatedAt)
                .execute()

            addNewTags(tags, ctx, task)
        }
        return task.copy(tags = tags?.toSet())
    }

    fun update(task: Task, tags: List<Tag>?): Task {
        dsl.transaction { configuration ->
            val ctx = DSL.using(configuration)
            ctx.update(TODO_TASK)
                .set(TODO_TASK.TITLE, task.title)
                .set(TODO_TASK.DESCRIPTION, task.description)
                .set(TODO_TASK.DONE, task.done)
                .set(TODO_TASK.UPDATED_AT, task.updatedAt)
                .where(TODO_TASK.ID.eq(task.id))
                .execute()

            ctx.deleteFrom(TODO_TASK_TAG)
                .where(TODO_TASK_TAG.TASK_ID.eq(task.id))
                .execute()

            addNewTags(tags, ctx, task)

            ctx.deleteFrom(TODO_TAG)
                .whereNotExists(
                    DSL.selectOne()
                        .from(TODO_TASK_TAG)
                        .where(TODO_TASK_TAG.TAG_ID.eq(TODO_TAG.ID))
                )
                .execute()
        }
        return task.copy(tags = tags?.toSet())
    }

    fun delete(id: UUID) {
        dsl.transaction { configuration ->
            val ctx = DSL.using(configuration)
            val tags = ctx.select(TODO_TASK_TAG.TAG_ID)
                .from(TODO_TASK_TAG)
                .where(TODO_TASK_TAG.TASK_ID.eq(id))
                .fetch(TODO_TASK_TAG.TAG_ID)

            ctx.deleteFrom(TODO_TASK_TAG)
                .where(TODO_TASK_TAG.TASK_ID.eq(id))
                .execute()

            ctx.deleteFrom(TODO_TASK)
                .where(TODO_TASK.ID.eq(id))
                .execute()

            ctx.deleteFrom(TODO_TAG)
                .where(TODO_TAG.ID.`in`(tags))
                .execute()
        }
    }

    private fun selectTasks() = dsl.select()
        .from(TODO_TASK)
        .leftJoin(TODO_TASK_TAG).on(TODO_TASK.ID.eq(TODO_TASK_TAG.TASK_ID))
        .leftJoin(TODO_TAG).on(TODO_TASK_TAG.TAG_ID.eq(TODO_TAG.ID))

    private fun toTask(
        taskRecord: TodoTaskRecord,
        tags: Result<TodoTagRecord>
    ) = taskRecord.into(Task::class.java)
        .copy(
            tags = tags.filter { r -> r.id != null }
                .map { record -> record.into(Tag::class.java) }
                .toSet()
        )

    private fun addNewTags(
        tags: List<Tag>?,
        ctx: DSLContext,
        task: Task
    ) {
        tags?.forEach { tag ->
            val tagIdOpt = ctx.select(TODO_TAG.ID)
                .from(TODO_TAG)
                .where(TODO_TAG.NAME.eq(tag.name))
                .fetchOne(TODO_TAG.ID)

            val tagId = tagIdOpt ?: ctx.insertInto(TODO_TAG)
                .set(TODO_TAG.ID, tag.id)
                .set(TODO_TAG.NAME, tag.name)
                .onConflictDoNothing()
                .returning(TODO_TAG.ID)
                .fetchOne()?.id ?: tag.id

            ctx.insertInto(TODO_TASK_TAG)
                .set(TODO_TASK_TAG.TASK_ID, task.id)
                .set(TODO_TASK_TAG.TAG_ID, tagId)
                .execute()
        }
    }
}
