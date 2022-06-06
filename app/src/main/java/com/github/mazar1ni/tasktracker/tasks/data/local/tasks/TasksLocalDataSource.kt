package com.github.mazar1ni.tasktracker.tasks.data.local.tasks

import com.github.mazar1ni.tasktracker.core.database.interfaces.BaseLocalDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TasksLocalDataSource(private val tasksDao: TasksDao) :
    BaseLocalDataSource<TasksEntity>(tasksDao) {

    suspend fun getAllWithNewerTimestamp(timestamp: Long): List<TasksEntity>? =
        withContext(Dispatchers.IO) {
            tasksDao.getAllWithNewerTimestamp(timestamp)
        }

    suspend fun deleteTasksIfNotPresentInList(tasksUUID: List<String>) =
        withContext(Dispatchers.IO) {
            if (tasksUUID.isNotEmpty())
                tasksDao.deleteTasksIfNotPresentInList(tasksUUID)
        }
}