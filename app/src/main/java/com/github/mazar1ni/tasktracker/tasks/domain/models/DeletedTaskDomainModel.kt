package com.github.mazar1ni.tasktracker.tasks.domain.models

import com.google.gson.annotations.SerializedName

data class DeletedTaskDomainModel(
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("timestamp")
    val timestamp: Long
)
