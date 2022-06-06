package com.github.mazar1ni.tasktracker.tasks.data.remote.request

import com.github.mazar1ni.tasktracker.tasks.data.remote.models.DeletedTaskDto
import com.github.mazar1ni.tasktracker.tasks.data.remote.models.TaskDto
import com.google.gson.annotations.SerializedName

data class SyncTasksRequest(
    @SerializedName("tasks")
    val taskDtos: List<TaskDto>?,
    @SerializedName("deleted_tasks_uuid")
    val deletedTasksUUID: List<DeletedTaskDto>?
)