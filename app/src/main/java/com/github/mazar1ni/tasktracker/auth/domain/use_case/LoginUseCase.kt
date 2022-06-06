package com.github.mazar1ni.tasktracker.auth.domain.use_case

import com.github.mazar1ni.tasktracker.auth.domain.repository.AuthRepository
import com.github.mazar1ni.tasktracker.auth.domain.states.LoginState
import com.github.mazar1ni.tasktracker.core.util.NetworkResultType
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val authRepository: AuthRepository) {

    companion object {
        const val MIN_CREDENTIAL_LENGTH = 8
        const val MAX_CREDENTIAL_LENGTH = 64
    }

    suspend operator fun invoke(username: String, password: String): LoginState {

        if (username.length < MIN_CREDENTIAL_LENGTH || username.length > MAX_CREDENTIAL_LENGTH)
            return LoginState.LoginUsernameLength

        if (password.length < MIN_CREDENTIAL_LENGTH || password.length > MAX_CREDENTIAL_LENGTH)
            return LoginState.LoginPasswordLength

        return when (authRepository.login(username, password)) {
            NetworkResultType.InternalError -> LoginState.LoginInternalError
            NetworkResultType.NoNetworkConnectivity -> LoginState.LoginNetworkConnectionError
            NetworkResultType.CredentialsIncorrect -> LoginState.LoginIncorrectCredentials
            NetworkResultType.Success -> LoginState.LoginSuccess
            else -> LoginState.LoginInternalError
        }
    }
}