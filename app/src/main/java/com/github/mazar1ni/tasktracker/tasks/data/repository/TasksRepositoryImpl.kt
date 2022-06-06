package com.github.mazar1ni.tasktracker.tasks.data.repository

import com.github.mazar1ni.tasktracker.core.NetworkResult
import com.github.mazar1ni.tasktracker.core.util.NetworkResultType
import com.github.mazar1ni.tasktracker.core.util.preferences.ApplicationPreferences
import com.github.mazar1ni.tasktracker.core.util.preferences.PreferencesType
import com.github.mazar1ni.tasktracker.tasks.data.local.delete_tasks.DeletedTasksLocalDataSource
import com.github.mazar1ni.tasktracker.tasks.data.local.tasks.TasksLocalDataSource
import com.github.mazar1ni.tasktracker.tasks.data.mappers.DeletedTaskMapper
import com.github.mazar1ni.tasktracker.tasks.data.mappers.TaskMapper
import com.github.mazar1ni.tasktracker.tasks.data.remote.TasksApi
import com.github.mazar1ni.tasktracker.tasks.data.remote.request.SyncTasksRequest
import com.github.mazar1ni.tasktracker.tasks.data.remote.response.SyncTasksResponse
import com.github.mazar1ni.tasktracker.tasks.domain.models.DeletedTaskDomainModel
import com.github.mazar1ni.tasktracker.tasks.domain.models.TaskDomainModel
import com.github.mazar1ni.tasktracker.tasks.domain.repository.TasksRepository

class TasksRepositoryImpl(
    private val tasksApi: TasksApi,
    private val applicationPreferences: ApplicationPreferences,
    private val tasksLocalDataSource: TasksLocalDataSource,
    private val deletedTasksLocalDataSource: DeletedTasksLocalDataSource
) : TasksRepository {

    override suspend fun getAll(): NetworkResult<List<TaskDomainModel>> =
        try {
            val result =
                tasksApi.getAllTasks("Bearer ${applicationPreferences.getString(PreferencesType.AccessToken)}")

            when (result.status) {
                402 -> NetworkResult(NetworkResultType.UsernameAlreadyFound)
                200 -> {
                    if (result.data == null || result.data.taskDtos.isEmpty())
                        NetworkResult(NetworkResultType.TaskListEmpty)
                    else
                        NetworkResult(
                            NetworkResultType.Success,
                            result.data.taskDtos.map { TaskMapper.dtoToDomain(it) })
                }
                else -> NetworkResult(NetworkResultType.InternalError)
            }
        } catch (exception: Exception) {
            NetworkResult(NetworkResultType.InternalError)
        }

    override suspend fun syncTasks(
        tasks: List<TaskDomainModel>,
        deletedTasksUUID: List<DeletedTaskDomainModel>
    ): NetworkResult<SyncTasksResponse> =
        try {
            val result =
                tasksApi.syncTasks(
                    "Bearer ${applicationPreferences.getString(PreferencesType.AccessToken)}",
                    SyncTasksRequest(
                        tasks.map { TaskMapper.domainToDto(it) },
                        deletedTasksUUID.map { DeletedTaskMapper.domainToDto(it) })
                )

            when (result.status) {
                402 -> NetworkResult(NetworkResultType.UsernameAlreadyFound)
                200 -> NetworkResult(NetworkResultType.Success, result.data)
                else -> NetworkResult(NetworkResultType.InternalError)
            }
        } catch (exception: Exception) {
            NetworkResult(NetworkResultType.InternalError)
        }

    override suspend fun saveTasksToDB(tasks: List<TaskDomainModel>) {
        tasksLocalDataSource.insertList(tasks.map { TaskMapper.domainToEntity(it) })
    }

    override suspend fun getAllTasksFromDB(): List<TaskDomainModel>? {
        return tasksLocalDataSource.getAll()?.map { TaskMapper.entityToDomain(it) }
    }

    override suspend fun getAllTasksNewerTimestampFromDB(timestamp: Long): List<TaskDomainModel>? {
        return tasksLocalDataSource.getAllWithNewerTimestamp(timestamp)
            ?.map { TaskMapper.entityToDomain(it) }
    }

    override suspend fun deleteTasksIfNotPresentInList(tasksUUID: List<String>) {
        tasksLocalDataSource.deleteTasksIfNotPresentInList(tasksUUID)
    }

    override suspend fun createTask(task: TaskDomainModel) {
        tasksLocalDataSource.insert(TaskMapper.domainToEntity(task))
    }

    override suspend fun deleteAllTasks() {
        tasksLocalDataSource.deleteAll()
    }

    override suspend fun deleteTask(task: TaskDomainModel) {
        tasksLocalDataSource.delete(TaskMapper.domainToEntity(task))
    }

    override suspend fun updateTask(task: TaskDomainModel) {
        tasksLocalDataSource.insert(TaskMapper.domainToEntity(task))
    }

    override suspend fun updateTasks(tasks: List<TaskDomainModel>) {
        tasksLocalDataSource.insertList(tasks.map { TaskMapper.domainToEntity(it) })
    }

    override suspend fun getTaskByUUID(id: Int): TaskDomainModel? {
        return tasksLocalDataSource.getById(id)?.let { TaskMapper.entityToDomain(it) }
    }

    override suspend fun getAllDeleteTasksFromDB(): List<DeletedTaskDomainModel>? {
        return deletedTasksLocalDataSource.getAll()?.map { DeletedTaskMapper.entityToDomain(it) }
    }

    override suspend fun addDeleteTasksToDB(deleteTask: DeletedTaskDomainModel) {
        deletedTasksLocalDataSource.insert(DeletedTaskMapper.domainToEntity(deleteTask))
    }

    override suspend fun removeDeleteTasksFromDB(deleteTasks: List<DeletedTaskDomainModel>) {
        deletedTasksLocalDataSource.deleteList(deleteTasks.map { DeletedTaskMapper.domainToEntity(it) })
    }
}