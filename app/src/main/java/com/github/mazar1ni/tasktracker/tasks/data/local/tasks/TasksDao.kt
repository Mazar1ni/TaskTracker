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
    fun deleteTasksIfNotPresentInList(tasksUUID: List<String>): Int {
        val deletedTasks = rawQueryCount(
            SimpleSQLiteQuery(
                "SELECT COUNT(*) FROM $tableName WHERE ${TasksEntity.UUID_FIELD_NAME} NOT IN ${
                    tasksUUID.joinToString(
                        prefix = "(",
                        postfix = ")",
                        separator = ",",
                        transform = { "\"$it\"" }
                    )
                }"
            )
        )


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

        return deletedTasks
    }

    @RawQuery
    fun insertOrReplaceWithoutId(tasks: List<TasksEntity>) =
        rawQuery(
            SimpleSQLiteQuery(
                "WITH new (${TasksEntity.TITLE_FIELD_NAME}, ${TasksEntity.DESCRIPTION_FIELD_NAME}, " +
                        "${TasksEntity.TIMESTAMP_FIELD_NAME}, ${TasksEntity.UUID_FIELD_NAME}, " +
                        "${TasksEntity.COMPLETED_FIELD_NAME}, ${TasksEntity.SYNCHRONIZED_FIELD_NAME}) " +
                        "AS ( VALUES ${
                            tasks.joinToString(
                                prefix = "",
                                postfix = "",
                                separator = ",",
                                transform = {
                                    "(\"${it.title}\", \"${it.description}\", ${it.timestamp}, " +
                                            "\"${it.uuid}\", ${if (it.isCompleted) 1 else 0}, 1)"
                                }
                            )
                        } ) " +

                        "INSERT or REPLACE INTO $tableName (${BaseEntity.ID_FIELD_NAME}, " +
                        "${TasksEntity.TITLE_FIELD_NAME}, ${TasksEntity.DESCRIPTION_FIELD_NAME}, " +
                        "${TasksEntity.TIMESTAMP_FIELD_NAME}, ${TasksEntity.UUID_FIELD_NAME}, " +
                        "${TasksEntity.COMPLETED_FIELD_NAME}, ${TasksEntity.SYNCHRONIZED_FIELD_NAME}) " +

                        "SELECT old.${BaseEntity.ID_FIELD_NAME}, new.${TasksEntity.TITLE_FIELD_NAME}, " +
                        "new.${TasksEntity.DESCRIPTION_FIELD_NAME}, new.${TasksEntity.TIMESTAMP_FIELD_NAME}, " +
                        "new.${TasksEntity.UUID_FIELD_NAME}, new.${TasksEntity.COMPLETED_FIELD_NAME}, " +
                        "new.${TasksEntity.SYNCHRONIZED_FIELD_NAME} FROM new LEFT JOIN $tableName " +
                        "AS old ON new.${TasksEntity.UUID_FIELD_NAME} = old.${TasksEntity.UUID_FIELD_NAME}"
            )
        )
}