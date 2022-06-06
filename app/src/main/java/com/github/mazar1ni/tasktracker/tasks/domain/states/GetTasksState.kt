package com.github.mazar1ni.tasktracker.tasks.domain.states

import com.github.mazar1ni.tasktracker.tasks.domain.models.TaskDomainModel

sealed class GetTasksState {
    object GetTasksInProgress : GetTasksState()

    object GetTasksNetworkConnectionError : GetTasksState()
    object GetTasksInternalError : GetTasksState()

    object GetTasksListEmpty : GetTasksState()

    class GetTasksSuccess(val tasks: List<TaskDomainModel>? = null) : GetTasksState()
}
