package com.github.mazar1ni.tasktracker.tasks.domain.use_case

import com.github.mazar1ni.tasktracker.core.util.NetworkResultType
import com.github.mazar1ni.tasktracker.core.util.preferences.ApplicationPreferences
import com.github.mazar1ni.tasktracker.core.util.preferences.PreferencesType
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
        else if (force || System.currentTimeMillis() - lastUpdateTasks >= 3600000) {
            syncTasksUseCase()
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
        val result = tasksRepository.getAllTasksFromDB()
        val deleteTasksList = tasksRepository.getAllDeleteTasksFromDB()

        if (lastUpdateTasks == null) {
            syncTasksUseCase()
        } else {
            if (result?.find { it.timestamp > lastUpdateTasks } != null ||
                !deleteTasksList.isNullOrEmpty()) {
                syncTasksUseCase()
            }
        }

        return if (result == null || result.isEmpty())
            TasksState.TasksListEmpty
        else
            TasksState.TasksSuccess(result)
    }

    private suspend fun syncTasksUseCase() {
        val lastUpdateTimestamp =
            applicationPreferences.getLong(PreferencesType.LastUpdateTasks)

        val needSyncTasks =
            if (lastUpdateTimestamp == null)
                tasksRepository.getAllTasksFromDB() ?: listOf()
            else
                tasksRepository.getAllTasksNewerTimestampFromDB(lastUpdateTimestamp) ?: listOf()

        val needSyncDeleteTasks = tasksRepository.getAllDeleteTasksFromDB() ?: listOf()

        if (needSyncTasks.isEmpty() && needSyncDeleteTasks.isEmpty())
            return

        val result = tasksRepository.syncTasks(needSyncTasks, needSyncDeleteTasks)

        needSyncTasks.forEach {
            it.synchronized = true
        }

        tasksRepository.updateTasks(needSyncTasks)
        tasksRepository.removeDeleteTasksFromDB(needSyncDeleteTasks)

        result.data?.uuidAllTasks?.let {
            if (it.isEmpty())
                tasksRepository.deleteAllTasks()
            else
                tasksRepository.deleteTasksIfNotPresentInList(it)
        }

        // TODO: request not found tasks

        when (result.networkResultType) {
            NetworkResultType.Success -> applicationPreferences.setLong(
                PreferencesType.LastUpdateTasks,
                System.currentTimeMillis()
            )
            else -> {
                // TODO: add logs
            }
        }
    }
}