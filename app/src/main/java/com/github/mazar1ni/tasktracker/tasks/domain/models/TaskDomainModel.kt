package com.github.mazar1ni.tasktracker.tasks.domain.models

import com.google.gson.annotations.SerializedName

data class TaskDomainModel(
    @SerializedName("title")
    var title: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("timestamp")
    var timestamp: Long,
    @SerializedName("synchronized")
    var synchronized: Boolean = false,
    @SerializedName("id")
    val id: Int? = null
)