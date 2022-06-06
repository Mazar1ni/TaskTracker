package com.github.mazar1ni.tasktracker.tasks.presentation.add_task

import android.text.Editable
import android.text.TextWatcher
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

    private var title = ""
    private var description = ""

    val titleWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(text: Editable?) {
            title = text.toString()
        }
    }

    val descriptionWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(text: Editable?) {
            description = text.toString()
        }
    }

    var stateAction: ((AddTaskState) -> Unit)? = null

    fun createTask() {
        stateAction?.invoke(AddTaskState.AddTaskStateInProgress)

        viewModelScope.launch {
            val result = createTaskUseCase(title, description)
            stateAction?.invoke(result)
        }
    }
}