package com.github.mazar1ni.tasktracker.tasks.data.remote.models

import com.google.gson.annotations.SerializedName

data class DeletedTaskDto(
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("timestamp")
    val timestamp: Long
)