package com.github.mazar1ni.tasktracker.tasks.data.mappers

import com.github.mazar1ni.tasktracker.tasks.data.local.tasks.TasksEntity
import com.github.mazar1ni.tasktracker.tasks.data.remote.models.TaskDto
import com.github.mazar1ni.tasktracker.tasks.domain.models.TaskDomainModel

object TaskMapper {

    fun entityToDomain(tasksEntity: TasksEntity) =
        TaskDomainModel(
            tasksEntity.title,
            tasksEntity.description,
            tasksEntity.uuid,
            tasksEntity.timestamp,
            tasksEntity.isSynchronized,
            tasksEntity.id
        )

    fun domainToEntity(taskDomainModel: TaskDomainModel) =
        TasksEntity(
            taskDomainModel.title,
            taskDomainModel.description,
            taskDomainModel.uuid,
            taskDomainModel.timestamp,
            taskDomainModel.synchronized
        ).apply { id = taskDomainModel.id }

    fun dtoToDomain(taskDto: TaskDto) =
        TaskDomainModel(
            taskDto.title,
            taskDto.description,
            taskDto.uuid,
            taskDto.timestamp
        )

    fun domainToDto(taskDomainModel: TaskDomainModel) =
        TaskDto(
            taskDomainModel.title,
            taskDomainModel.description,
            taskDomainModel.uuid,
            taskDomainModel.timestamp
        )

}