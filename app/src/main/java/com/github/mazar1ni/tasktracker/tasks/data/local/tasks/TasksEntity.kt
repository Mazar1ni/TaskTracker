package com.github.mazar1ni.tasktracker.tasks.data.local.tasks

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.github.mazar1ni.tasktracker.core.database.interfaces.BaseEntity

@Entity(tableName = TasksDao.TABLE_NAME)
data class TasksEntity(
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = UUID_FIELD_NAME)
    val uuid: String,
    @ColumnInfo(name = TIMESTAMP_FIELD_NAME)
    val timestamp: Long,
    @ColumnInfo(name = "synchronized")
    var isSynchronized: Boolean = false
) : BaseEntity() {
    companion object {
        const val TIMESTAMP_FIELD_NAME = "timestamp"
        const val UUID_FIELD_NAME = "uuid"
    }
}