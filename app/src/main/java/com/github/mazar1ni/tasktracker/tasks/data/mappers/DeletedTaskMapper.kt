package com.github.mazar1ni.tasktracker.tasks.data.mappers

import com.github.mazar1ni.tasktracker.tasks.data.local.delete_tasks.DeletedTasksEntity
import com.github.mazar1ni.tasktracker.tasks.data.remote.models.DeletedTaskDto
import com.github.mazar1ni.tasktracker.tasks.domain.models.DeletedTaskDomainModel

object DeletedTaskMapper {

    fun entityToDomain(deletedTasksEntity: DeletedTasksEntity) =
        DeletedTaskDomainModel(
            deletedTasksEntity.uuid,
            deletedTasksEntity.timestamp
        )

    fun domainToEntity(deletedTaskDomainModel: DeletedTaskDomainModel) =
        DeletedTasksEntity(
            deletedTaskDomainModel.uuid,
            deletedTaskDomainModel.timestamp
        )

    fun dtoToDomain(deletedTaskDto: DeletedTaskDto) =
        DeletedTaskDomainModel(
            deletedTaskDto.uuid,
            deletedTaskDto.timestamp
        )

    fun domainToDto(deletedTaskDomainModel: DeletedTaskDomainModel) =
        DeletedTaskDto(
            deletedTaskDomainModel.uuid,
            deletedTaskDomainModel.timestamp
        )

}