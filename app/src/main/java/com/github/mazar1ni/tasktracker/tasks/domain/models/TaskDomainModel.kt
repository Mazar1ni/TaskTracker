package com.github.mazar1ni.tasktracker.tasks.domain.models

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class TaskDomainModel(
    @SerializedName("title")
    var title: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("due_date")
    var dueDate: Long?,
    @SerializedName("has_time")
    var hasTime: Boolean,
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("timestamp")
    var timestamp: Long,
    @ColumnInfo(name = "completed")
    var isCompleted: Boolean = false,
    @SerializedName("synchronized")
    var isSynchronized: Boolean = false,
    @SerializedName("id")
    val id: Int? = null
)