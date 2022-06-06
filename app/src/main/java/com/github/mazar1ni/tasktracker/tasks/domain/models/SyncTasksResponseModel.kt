package com.github.mazar1ni.tasktracker.tasks.domain.models

import com.google.gson.annotations.SerializedName

data class SyncTasksResponseModel(
    @SerializedName("all_tasks_uuid")
    val uuidAllTasks: List<String>?,
    @SerializedName("updated_tasks")
    val updatedTasks: List<TaskDomainModel>?
)