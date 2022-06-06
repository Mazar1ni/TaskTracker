package com.github.mazar1ni.tasktracker.auth.data.repository

import com.github.mazar1ni.tasktracker.auth.data.remote.AuthApi
import com.github.mazar1ni.tasktracker.auth.data.remote.request.LoginRequest
import com.github.mazar1ni.tasktracker.auth.data.remote.request.RegisterRequest
import com.github.mazar1ni.tasktracker.auth.domain.repository.AuthRepository
import com.github.mazar1ni.tasktracker.core.util.NetworkResultType
import com.github.mazar1ni.tasktracker.core.util.preferences.ApplicationPreferences
import com.github.mazar1ni.tasktracker.core.util.preferences.PreferencesType

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val applicationPreferences: ApplicationPreferences
) : AuthRepository {

    override suspend fun register(email: String, username: String, password: String) =
        try {
            val result = authApi.register(RegisterRequest(email, username, password))

            when (result.status) {
                402 -> NetworkResultType.UsernameAlreadyFound
                200 -> {
                    if (result.data == null ||
                        result.data.accessToken.isNullOrEmpty() ||
                        result.data.refreshToken.isNullOrEmpty()
                    ) {
                        NetworkResultType.InternalError
                    } else {
                        applicationPreferences.setString(
                            PreferencesType.AccessToken,
                            result.data.accessToken
                        )
                        applicationPreferences.setString(
                            PreferencesType.RefreshToken,
                            result.data.refreshToken
                        )
                        NetworkResultType.Success
                    }
                }
                else -> NetworkResultType.InternalError
            }
        } catch (exception: Exception) {
            NetworkResultType.InternalError
        }

    override suspend fun login(username: String, password: String) =
        try {
            val result = authApi.login(LoginRequest(username, password))

            when (result.status) {
                401 -> NetworkResultType.CredentialsIncorrect
                200 -> {
                    if (result.data == null ||
                        result.data.accessToken.isNullOrEmpty() ||
                        result.data.refreshToken.isNullOrEmpty()
                    ) {
                        NetworkResultType.InternalError
                    } else {
                        applicationPreferences.setString(
                            PreferencesType.AccessToken,
                            result.data.accessToken
                        )
                        applicationPreferences.setString(
                            PreferencesType.RefreshToken,
                            result.data.refreshToken
                        )
                        NetworkResultType.Success
                    }
                }
                else -> NetworkResultType.InternalError
            }
        } catch (exception: Exception) {
            NetworkResultType.InternalError
        }
}