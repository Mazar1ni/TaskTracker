package com.github.mazar1ni.tasktracker.tasks.data.mappers

import com.github.mazar1ni.tasktracker.tasks.data.local.tasks.TasksEntity
import com.github.mazar1ni.tasktracker.tasks.data.remote.models.TaskDto
import com.github.mazar1ni.tasktracker.tasks.domain.models.TaskDomainModel

object TaskMapper {

    fun entityToDomain(tasksEntity: TasksEntity) =
        TaskDomainModel(
            tasksEntity.title,
            tasksEntity.description,
            tasksEntity.dueDate,
            tasksEntity.hasTime,
            tasksEntity.uuid,
            tasksEntity.timestamp,
            tasksEntity.isCompleted,
            tasksEntity.isSynchronized,
            tasksEntity.id
        )

    fun domainToEntity(taskDomainModel: TaskDomainModel) =
        TasksEntity(
            taskDomainModel.title,
            taskDomainModel.description,
            taskDomainModel.dueDate,
            taskDomainModel.hasTime,
            taskDomainModel.uuid,
            taskDomainModel.timestamp,
            taskDomainModel.isCompleted,
            taskDomainModel.isSynchronized
        ).apply { id = taskDomainModel.id }

    fun dtoToDomain(taskDto: TaskDto) =
        TaskDomainModel(
            taskDto.title,
            taskDto.description,
            taskDto.dueDate,
            taskDto.hasTime,
            taskDto.uuid,
            taskDto.timestamp,
            taskDto.isCompleted
        )

    fun domainToDto(taskDomainModel: TaskDomainModel) =
        TaskDto(
            taskDomainModel.title,
            taskDomainModel.description,
            taskDomainModel.dueDate,
            taskDomainModel.hasTime,
            taskDomainModel.uuid,
            taskDomainModel.timestamp,
            taskDomainModel.isCompleted
        )

}