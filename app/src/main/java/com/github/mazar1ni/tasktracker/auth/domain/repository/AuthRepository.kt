package com.github.mazar1ni.tasktracker.auth.domain.repository

import com.github.mazar1ni.tasktracker.core.util.NetworkResultType

interface AuthRepository {

    suspend fun register(
        email: String,
        username: String,
        password: String
    ): NetworkResultType

    suspend fun login(
        username: String,
        password: String
    ): NetworkResultType

}