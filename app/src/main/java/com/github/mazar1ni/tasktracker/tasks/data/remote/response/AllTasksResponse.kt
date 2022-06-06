package com.github.mazar1ni.tasktracker.tasks.data.remote.response

import com.github.mazar1ni.tasktracker.tasks.data.remote.models.TaskDto
import com.google.gson.annotations.SerializedName

data class AllTasksResponse(
    @SerializedName("tasks")
    val taskDtos: List<TaskDto>
)