package com.github.mazar1ni.tasktracker.tasks.data.local.tasks

import androidx.room.Dao
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import com.github.mazar1ni.tasktracker.core.database.interfaces.BaseDao
import com.github.mazar1ni.tasktracker.core.database.interfaces.BaseEntity

@Dao
abstract class TasksDao : BaseDao<TasksEntity>(TABLE_NAME) {
    companion object {
        const val TABLE_NAME = "Tasks"
    }

    @RawQuery
    fun getAllWithNewerTimestamp(timestamp: Long): List<TasksEntity>? =
        rawQuery(SimpleSQLiteQuery("SELECT * FROM $tableName WHERE ${TasksEntity.TIMESTAMP_FIELD_NAME} > $timestamp"))

    @RawQuery
    fun deleteTasksIfNotPresentInList(tasksUUID: List<String>) =
        rawQuery(
            SimpleSQLiteQuery(
                "DELETE FROM $tableName WHERE ${TasksEntity.UUID_FIELD_NAME} NOT IN ${
                    tasksUUID.joinToString(
                        prefix = "(",
                        postfix = ")",
                        separator = ",",
                        transform = { "\"$it\"" }
                    )
                }"
            )
        )

    @RawQuery
    fun insertWithoutId(task: TasksEntity) =
        rawQuery(
            SimpleSQLiteQuery(
                "INSERT or REPLACE INTO $tableName (${BaseEntity.ID_FIELD_NAME}, " +
                        "${TasksEntity.TITLE_FIELD_NAME}, ${TasksEntity.DESCRIPTION_FIELD_NAME}, " +
                        "${TasksEntity.TIMESTAMP_FIELD_NAME}, ${TasksEntity.UUID_FIELD_NAME}, " +
                        "${TasksEntity.COMPLETED_FIELD_NAME}, ${TasksEntity.SYNCHRONIZED_FIELD_NAME}) " +
                        "SELECT ${BaseEntity.ID_FIELD_NAME}, \"${task.title}\", \"${task.description}\", " +
                        "${task.timestamp}, \"${task.uuid}\", ${if (task.isCompleted) 1 else 0}, " +
                        "1 FROM $tableName " +
                        "WHERE ${TasksEntity.UUID_FIELD_NAME} = \"${task.uuid}\""
            )
        )
}