package com.github.mazar1ni.tasktracker.tasks.domain.states

import com.github.mazar1ni.tasktracker.tasks.domain.models.TaskDomainModel

sealed class TasksState {
    object TasksInProgress : TasksState()

    object TasksNetworkConnectionError : TasksState()
    object TasksInternalError : TasksState()

    object TasksListEmpty : TasksState()

    object TasksCompletedSuccess : TasksState()

    class TasksSuccess(val tasks: List<TaskDomainModel>? = null) : TasksState()
}
