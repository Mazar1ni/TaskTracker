package com.github.mazar1ni.tasktracker.tasks.presentation.tasks_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mazar1ni.tasktracker.tasks.domain.states.TasksState
import com.github.mazar1ni.tasktracker.tasks.domain.use_case.CompleteTaskUseCase
import com.github.mazar1ni.tasktracker.tasks.domain.use_case.SyncTasksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val syncTasksUseCase: SyncTasksUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase
) : ViewModel() {

    var stateAction: ((TasksState) -> Unit)? = null

    fun updateListTasks(force: Boolean = false) {
        stateAction?.invoke(TasksState.TasksInProgress)
        viewModelScope.launch {
            stateAction?.invoke(syncTasksUseCase(force))
        }
    }

    fun completeTask(taskId: Int, isChecked: Boolean) {
        stateAction?.invoke(TasksState.TasksInProgress)
        viewModelScope.launch {
            stateAction?.invoke(completeTaskUseCase(taskId, isChecked))
        }
    }
}