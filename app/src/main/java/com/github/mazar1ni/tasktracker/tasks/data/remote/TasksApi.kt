package com.github.mazar1ni.tasktracker.tasks.data.remote

import com.github.mazar1ni.tasktracker.core.data.response.ApiResponse
import com.github.mazar1ni.tasktracker.tasks.data.remote.request.SyncTasksRequest
import com.github.mazar1ni.tasktracker.tasks.data.remote.response.AllTasksResponse
import com.github.mazar1ni.tasktracker.tasks.data.remote.response.SyncTasksResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface TasksApi {

    @GET("/tasks/getAll")
    suspend fun getAllTasks(
        @Header("Authorization")
        authorization: String
    ): ApiResponse<AllTasksResponse>

    @POST("/tasks/syncTasks")
    suspend fun syncTasks(
        @Header("Authorization")
        authorization: String,
        @Body
        syncTasksRequest: SyncTasksRequest
    ): ApiResponse<SyncTasksResponse>
}