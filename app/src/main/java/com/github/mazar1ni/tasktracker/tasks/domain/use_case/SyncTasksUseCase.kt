package com.github.mazar1ni.tasktracker.tasks.domain.use_case

import com.github.mazar1ni.tasktracker.core.NetworkResult
import com.github.mazar1ni.tasktracker.core.util.Constants
import com.github.mazar1ni.tasktracker.core.util.NetworkResultType
import com.github.mazar1ni.tasktracker.core.util.preferences.ApplicationPreferences
import com.github.mazar1ni.tasktracker.core.util.preferences.PreferencesType
import com.github.mazar1ni.tasktracker.tasks.domain.models.DeletedTaskDomainModel
import com.github.mazar1ni.tasktracker.tasks.domain.models.SyncTasksResponseModel
import com.github.mazar1ni.tasktracker.tasks.domain.models.TaskDomainModel
import com.github.mazar1ni.tasktracker.tasks.domain.repository.TasksRepository
import com.github.mazar1ni.tasktracker.tasks.domain.states.TasksState
import javax.inject.Inject

class SyncTasksUseCase @Inject constructor(
    private val tasksRepository: TasksRepository,
    private val applicationPreferences: ApplicationPreferences
) {

    suspend operator fun invoke(force: Boolean): TasksState {
        val lastUpdateTasks = applicationPreferences.getLong(PreferencesType.LastUpdateTasks)
        return if (lastUpdateTasks == null)
            getAllTasks()
        else if (force || System.currentTimeMillis() - lastUpdateTasks >= Constants.UPDATE_TASKS_TIME_MS) {
            syncTasks()
            getTasksFromDB(lastUpdateTasks)
        } else
            getTasksFromDB(lastUpdateTasks)
    }

    private suspend fun getAllTasks(): TasksState {
        val result = tasksRepository.getAll()
        if (result.networkResultType == NetworkResultType.Success) {
            applicationPreferences.setLong(
                PreferencesType.LastUpdateTasks,
                System.currentTimeMillis()
            )

            if (result.data == null || result.data.isEmpty())
                TasksState.TasksSuccess()
            else {
                tasksRepository.saveTasksToDB(result.data)
                TasksState.TasksSuccess()
            }
        }

        return getTasksFromDB(null)
    }

    private suspend fun getTasksFromDB(lastUpdateTasks: Long?): TasksState {
        var result = tasksRepository.getAllTasksFromDB()
        val deleteTasksList = tasksRepository.getAllDeleteTasksFromDB()

        val needUpdateAllListInUI = if (lastUpdateTasks == null) {
            syncTasks()
        } else {
            if (result?.find { it.timestamp > lastUpdateTasks } != null ||
                !deleteTasksList.isNullOrEmpty()) {
                syncTasks()
            } else
                false
        }

        if (needUpdateAllListInUI) {
            result = tasksRepository.getAllTasksFromDB()
        }

        return if (result == null || result.isEmpty())
            TasksState.TasksListEmpty
        else
            TasksState.TasksSuccess(result)
    }

    private suspend fun syncTasks(): Boolean {
        val lastUpdateTimestamp =
            applicationPreferences.getLong(PreferencesType.LastUpdateTasks)

        val needSyncTasks =
            if (lastUpdateTimestamp == null)
                tasksRepository.getAllTasksFromDB() ?: listOf()
            else
                tasksRepository.getAllTasksNewerTimestampFromDB(lastUpdateTimestamp) ?: listOf()

        val needSyncDeleteTasks = tasksRepository.getAllDeleteTasksFromDB() ?: listOf()

        if (needSyncTasks.isEmpty() && needSyncDeleteTasks.isEmpty())
            return false

        val result =
            tasksRepository.syncTasks(needSyncTasks, needSyncDeleteTasks, lastUpdateTimestamp)

        return when (result.networkResultType) {
            NetworkResultType.Success -> {
                applyChanges(result, needSyncTasks, needSyncDeleteTasks)
            }
            else -> {
                // TODO: add logs
                false
            }
        }
    }

    private suspend fun applyChanges(
        result: NetworkResult<SyncTasksResponseModel>,
        needSyncTasks: List<TaskDomainModel>,
        needSyncDeleteTasks: List<DeletedTaskDomainModel>
    ): Boolean {

        needSyncTasks.forEach {
            it.isSynchronized = true
        }

        tasksRepository.updateTasks(needSyncTasks)
        tasksRepository.removeDeleteTasksFromDB(needSyncDeleteTasks)

        var needUpdateAllListInUI = false

        result.data?.uuidAllTasks?.let {
            if (it.isEmpty())
                tasksRepository.deleteAllTasks()
            else {
                val deletedTasksCount = tasksRepository.deleteTasksIfNotPresentInList(it)
                if (deletedTasksCount > 0)
                    needUpdateAllListInUI = true
            }
        }

        if (result.data?.updatedTasks?.isNotEmpty() == true)
            needUpdateAllListInUI = true

        result.data?.updatedTasks?.let { it ->
            tasksRepository.updateTaskWithoutId(it)
        }

        applicationPreferences.setLong(
            PreferencesType.LastUpdateTasks,
            System.currentTimeMillis()
        )

        return needUpdateAllListInUI
    }
}