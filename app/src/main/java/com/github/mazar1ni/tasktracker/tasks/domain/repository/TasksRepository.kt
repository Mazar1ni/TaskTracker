package com.github.mazar1ni.tasktracker.tasks.domain.repository

import com.github.mazar1ni.tasktracker.core.NetworkResult
import com.github.mazar1ni.tasktracker.tasks.data.remote.response.SyncTasksResponse
import com.github.mazar1ni.tasktracker.tasks.domain.models.DeletedTaskDomainModel
import com.github.mazar1ni.tasktracker.tasks.domain.models.TaskDomainModel

interface TasksRepository {

    suspend fun getAll(): NetworkResult<List<TaskDomainModel>>

    suspend fun syncTasks(
        tasks: List<TaskDomainModel>,
        deletedTasksUUID: List<DeletedTaskDomainModel>
    ): NetworkResult<SyncTasksResponse>

    suspend fun saveTasksToDB(tasks: List<TaskDomainModel>)

    suspend fun getAllTasksFromDB(): List<TaskDomainModel>?

    suspend fun getAllTasksNewerTimestampFromDB(timestamp: Long): List<TaskDomainModel>?

    suspend fun deleteTasksIfNotPresentInList(tasksUUID: List<String>)

    suspend fun createTask(task: TaskDomainModel)

    suspend fun deleteAllTasks()

    suspend fun deleteTask(task: TaskDomainModel)

    suspend fun updateTask(task: TaskDomainModel)

    suspend fun updateTasks(tasks: List<TaskDomainModel>)

    suspend fun getTaskByUUID(id: Int): TaskDomainModel?

    suspend fun getAllDeleteTasksFromDB(): List<DeletedTaskDomainModel>?

    suspend fun addDeleteTasksToDB(deleteTask: DeletedTaskDomainModel)

    suspend fun removeDeleteTasksFromDB(deleteTasks: List<DeletedTaskDomainModel>)
}