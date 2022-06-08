package com.github.mazar1ni.tasktracker.tasks.presentation.add_task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mazar1ni.tasktracker.core.util.Utils
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
    var dueDate: Long? = null
    var dueHour: Int? = null
    var dueMinute: Int? = null

    var stateAction: ((AddTaskState) -> Unit)? = null

    fun createTask() {
        stateAction?.invoke(AddTaskState.AddTaskStateInProgress)

        viewModelScope.launch {
            val date = dueDate?.run {
                Utils.localDateTimeToEpoch(this, dueHour, dueMinute)
            }

            val hasTime = dueHour != null && dueMinute != null

            val result = createTaskUseCase(title, description, date, hasTime)
            stateAction?.invoke(result)
        }
    }
}