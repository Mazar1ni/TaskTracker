package com.github.mazar1ni.tasktracker.tasks.domain.states

sealed class AddTaskState {
    object AddTaskStateInProgress : AddTaskState()
    object AddTaskStateEmptyTitle : AddTaskState()
    object AddTaskStateSuccess : AddTaskState()
}
