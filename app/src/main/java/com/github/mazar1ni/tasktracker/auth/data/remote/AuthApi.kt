package com.github.mazar1ni.tasktracker.auth.data.remote

import com.github.mazar1ni.tasktracker.auth.data.remote.request.LoginRequest
import com.github.mazar1ni.tasktracker.auth.data.remote.request.RegisterRequest
import com.github.mazar1ni.tasktracker.auth.data.remote.response.LoginResponse
import com.github.mazar1ni.tasktracker.core.data.response.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("/user/login")
    suspend fun login(
        @Body request: LoginRequest
    ): ApiResponse<LoginResponse>

    @POST("/user/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): ApiResponse<LoginResponse>

}