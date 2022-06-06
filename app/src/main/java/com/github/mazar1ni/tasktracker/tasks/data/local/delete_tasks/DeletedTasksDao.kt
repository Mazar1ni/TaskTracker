package com.github.mazar1ni.tasktracker.tasks.data.local.delete_tasks

import androidx.room.Dao
import com.github.mazar1ni.tasktracker.core.database.interfaces.BaseDao

@Dao
abstract class DeletedTasksDao : BaseDao<DeletedTasksEntity>(TABLE_NAME) {
    companion object {
        const val TABLE_NAME = "DeletedTasks"
    }
}