package com.github.mazar1ni.tasktracker.tasks.presentation.tasks_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mazar1ni.tasktracker.tasks.domain.states.GetTasksState
import com.github.mazar1ni.tasktracker.tasks.domain.use_case.SyncTasksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val syncTasksUseCase: SyncTasksUseCase
) : ViewModel() {

    var stateAction: ((GetTasksState) -> Unit)? = null

    fun updateListTasks(force: Boolean = false) {
        stateAction?.invoke(GetTasksState.GetTasksInProgress)
        viewModelScope.launch {
            stateAction?.invoke(syncTasksUseCase(force))
        }
    }
}