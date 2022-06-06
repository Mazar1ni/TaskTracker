package com.github.mazar1ni.tasktracker.tasks.data.remote.response

import com.github.mazar1ni.tasktracker.tasks.data.remote.models.TaskDto
import com.google.gson.annotations.SerializedName

data class SyncTasksResponse(
    @SerializedName("all_tasks_uuid")
    val uuidAllTasks: List<String>?,
    @SerializedName("updated_tasks")
    val updatedTasks: List<TaskDto>?
)