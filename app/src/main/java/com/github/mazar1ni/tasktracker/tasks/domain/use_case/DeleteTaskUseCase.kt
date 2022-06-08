package com.github.mazar1ni.tasktracker.tasks.domain.use_case

import com.github.mazar1ni.tasktracker.tasks.domain.models.DeletedTaskDomainModel
import com.github.mazar1ni.tasktracker.tasks.domain.models.TaskDomainModel
import com.github.mazar1ni.tasktracker.tasks.domain.repository.TasksRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val tasksRepository: TasksRepository
) {

    suspend operator fun invoke(taskDomainModel: TaskDomainModel) {
        tasksRepository.deleteTask(taskDomainModel)

        if (taskDomainModel.isSynchronized) {
            tasksRepository.addDeleteTasksToDB(
                DeletedTaskDomainModel(
                    taskDomainModel.uuid,
                    System.currentTimeMillis()
                )
            )
        }
    }
}