package com.github.mazar1ni.tasktracker.core.data.response

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("status")
    val status: Int,
    @SerializedName("data")
    val data: T? = null
)