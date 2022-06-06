package com.github.mazar1ni.tasktracker.tasks.domain.states

sealed class EditTaskState {
    object EditTaskStateSuccess : EditTaskState()
}
