package com.github.mazar1ni.tasktracker.tasks.data.remote.models

import com.google.gson.annotations.SerializedName

data class TaskDto(
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("due_date")
    val dueDate: Long?,
    @SerializedName("has_time")
    val hasTime: Boolean,
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("timestamp")
    val timestamp: Long,
    @SerializedName("completed")
    val isCompleted: Boolean
)
