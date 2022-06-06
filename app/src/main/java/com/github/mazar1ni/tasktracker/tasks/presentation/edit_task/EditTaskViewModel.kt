package com.github.mazar1ni.tasktracker.tasks.presentation.edit_task

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mazar1ni.tasktracker.tasks.domain.models.TaskDomainModel
import com.github.mazar1ni.tasktracker.tasks.domain.states.EditTaskState
import com.github.mazar1ni.tasktracker.tasks.domain.use_case.DeleteTaskUseCase
import com.github.mazar1ni.tasktracker.tasks.domain.use_case.GetTaskByIdUseCase
import com.github.mazar1ni.tasktracker.tasks.domain.use_case.SaveEditTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val saveEditTaskUseCase: SaveEditTaskUseCase
) : ViewModel() {

    private val _taskDomainModel = MutableLiveData<TaskDomainModel>()
    val taskDomainModel: LiveData<TaskDomainModel> = _taskDomainModel

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

    var stateAction: ((EditTaskState) -> Unit)? = null

    fun getTaskByUUID(id: Int) {
        viewModelScope.launch {
            getTaskByIdUseCase(id)?.let { task ->
                _taskDomainModel.value = task
            }
        }
    }

    fun deleteTask() {
        _taskDomainModel.value?.let {
            viewModelScope.launch {
                deleteTaskUseCase(it)
                stateAction?.invoke(EditTaskState.EditTaskStateSuccess)
            }
        }
    }

    fun saveEditTask() {
        _taskDomainModel.value?.let {
            CoroutineScope(Dispatchers.IO).launch {
                if (title != it.title || description != it.description) {
                    saveEditTaskUseCase(it.apply {
                        title = this@EditTaskViewModel.title
                        description = this@EditTaskViewModel.description
                    })
                    stateAction?.invoke(EditTaskState.EditTaskStateSuccess)
                }
            }
        }
    }
}