package com.github.mazar1ni.tasktracker.tasks.data.local.tasks

import androidx.room.Dao
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import com.github.mazar1ni.tasktracker.core.database.interfaces.BaseDao

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
}