package com.github.mazar1ni.tasktracker.tasks.presentation.add_task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mazar1ni.tasktracker.tasks.domain.states.AddTaskState
import com.github.mazar1ni.tasktracker.tasks.domain.use_case.CreateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val createTaskUseCase: CreateTaskUseCase,
) : ViewModel() {

    var title = ""
    var description = ""

    var stateAction: ((AddTaskState) -> Unit)? = null

    fun createTask() {
        stateAction?.invoke(AddTaskState.AddTaskStateInProgress)

        viewModelScope.launch {
            val result = createTaskUseCase(title, description)
            stateAction?.invoke(result)
        }
    }
}