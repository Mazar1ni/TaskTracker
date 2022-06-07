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

    suspend fun deleteTasksIfNotPresentInList(tasksUUID: List<String>): Int =
        withContext(Dispatchers.IO) {
            if (tasksUUID.isNotEmpty())
                return@withContext tasksDao.deleteTasksIfNotPresentInList(tasksUUID)
            else
                return@withContext 0
        }

    suspend fun insertOrReplaceWithoutId(tasks: List<TasksEntity>) =
        withContext(Dispatchers.IO) {
            if (tasks.isNotEmpty())
                tasksDao.insertOrReplaceWithoutId(tasks)
        }
}