package com.github.mazar1ni.tasktracker.tasks.domain.use_case

import com.github.mazar1ni.tasktracker.tasks.domain.models.TaskDomainModel
import com.github.mazar1ni.tasktracker.tasks.domain.repository.TasksRepository
import com.github.mazar1ni.tasktracker.tasks.domain.states.AddTaskState
import java.util.*
import javax.inject.Inject

class CreateTaskUseCase @Inject constructor(
    private val tasksRepository: TasksRepository
) {

    suspend operator fun invoke(
        title: String,
        description: String,
        dueDate: Long?,
        hasTime: Boolean
    ): AddTaskState {

        if (title.isEmpty())
            return AddTaskState.AddTaskStateEmptyTitle

        tasksRepository.createTask(
            TaskDomainModel(
                title,
                description,
                dueDate,
                hasTime,
                UUID.randomUUID().toString(),
                System.currentTimeMillis()
            )
        )
        return AddTaskState.AddTaskStateSuccess
    }
}