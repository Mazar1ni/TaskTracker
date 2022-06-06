package com.github.mazar1ni.tasktracker.tasks.domain.use_case

import com.github.mazar1ni.tasktracker.tasks.domain.repository.TasksRepository
import com.github.mazar1ni.tasktracker.tasks.domain.states.TasksState
import javax.inject.Inject

class CompleteTaskUseCase @Inject constructor(
    private val tasksRepository: TasksRepository
) {

    suspend operator fun invoke(taskId: Int, isChecked: Boolean): TasksState {
        val task = tasksRepository.getTaskByUUID(taskId)
        return if (task == null) {
            TasksState.TasksInternalError
        } else {
            task.isCompleted = isChecked
            task.timestamp = System.currentTimeMillis()
            tasksRepository.updateTask(task)
            TasksState.TasksCompletedSuccess
        }
    }
}