package com.github.mazar1ni.tasktracker.tasks.domain.use_case

import com.github.mazar1ni.tasktracker.tasks.domain.models.TaskDomainModel
import com.github.mazar1ni.tasktracker.tasks.domain.repository.TasksRepository
import javax.inject.Inject

class SaveEditTaskUseCase @Inject constructor(
    private val tasksRepository: TasksRepository
) {

    suspend operator fun invoke(task: TaskDomainModel) {
        tasksRepository.updateTask(task.apply {
            timestamp = System.currentTimeMillis()
        })
    }
}