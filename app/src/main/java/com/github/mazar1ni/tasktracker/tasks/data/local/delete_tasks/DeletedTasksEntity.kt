package com.github.mazar1ni.tasktracker.tasks.data.local.delete_tasks

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.github.mazar1ni.tasktracker.core.database.interfaces.BaseEntity

@Entity(tableName = DeletedTasksDao.TABLE_NAME)
data class DeletedTasksEntity(
    @ColumnInfo(name = "uuid")
    val uuid: String,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long
) : BaseEntity() {
}