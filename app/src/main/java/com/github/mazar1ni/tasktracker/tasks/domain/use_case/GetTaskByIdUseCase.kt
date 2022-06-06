package com.github.mazar1ni.tasktracker.tasks.domain.use_case

import com.github.mazar1ni.tasktracker.tasks.domain.models.TaskDomainModel
import com.github.mazar1ni.tasktracker.tasks.domain.repository.TasksRepository
import javax.inject.Inject

class GetTaskByIdUseCase @Inject constructor(
    private val tasksRepository: TasksRepository
) {

    suspend operator fun invoke(id: Int): TaskDomainModel? {
        return tasksRepository.getTaskByUUID(id)
    }
}