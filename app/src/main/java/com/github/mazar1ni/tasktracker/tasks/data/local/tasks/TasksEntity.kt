package com.github.mazar1ni.tasktracker.tasks.data.local.tasks

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.github.mazar1ni.tasktracker.core.database.interfaces.BaseEntity

@Entity(tableName = TasksDao.TABLE_NAME)
data class TasksEntity(
    @ColumnInfo(name = TITLE_FIELD_NAME)
    val title: String,
    @ColumnInfo(name = DESCRIPTION_FIELD_NAME)
    val description: String,
    @ColumnInfo(name = UUID_FIELD_NAME)
    val uuid: String,
    @ColumnInfo(name = TIMESTAMP_FIELD_NAME)
    val timestamp: Long,
    @ColumnInfo(name = COMPLETED_FIELD_NAME)
    val isCompleted: Boolean,
    @ColumnInfo(name = SYNCHRONIZED_FIELD_NAME)
    var isSynchronized: Boolean = false
) : BaseEntity() {
    companion object {
        const val TITLE_FIELD_NAME = "title"
        const val DESCRIPTION_FIELD_NAME = "description"
        const val TIMESTAMP_FIELD_NAME = "timestamp"
        const val UUID_FIELD_NAME = "uuid"
        const val COMPLETED_FIELD_NAME = "completed"
        const val SYNCHRONIZED_FIELD_NAME = "synchronized"
    }
}