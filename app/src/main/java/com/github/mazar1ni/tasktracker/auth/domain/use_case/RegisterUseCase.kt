package com.github.mazar1ni.tasktracker.auth.domain.use_case

import com.github.mazar1ni.tasktracker.auth.domain.repository.AuthRepository
import com.github.mazar1ni.tasktracker.auth.domain.states.RegisterState
import com.github.mazar1ni.tasktracker.core.util.NetworkResultType
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val authRepository: AuthRepository) {

    companion object {
        const val MIN_CREDENTIAL_LENGTH = 8
        const val MAX_CREDENTIAL_LENGTH = 64
    }

    suspend operator fun invoke(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): RegisterState {

        if (username.length < MIN_CREDENTIAL_LENGTH || username.length > MAX_CREDENTIAL_LENGTH)
            return RegisterState.RegisterUsernameLength

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return RegisterState.RegisterEmailIncorrect

        if (password.length < MIN_CREDENTIAL_LENGTH || password.length > MAX_CREDENTIAL_LENGTH)
            return RegisterState.RegisterPasswordLength

        if (confirmPassword.length < MIN_CREDENTIAL_LENGTH || confirmPassword.length > MAX_CREDENTIAL_LENGTH)
            return RegisterState.RegisterConfirmPasswordLength

        if (password != confirmPassword)
            return RegisterState.RegisterPasswordsDoNotMatch

        return when (authRepository.register(email, username, password)) {
            NetworkResultType.InternalError -> RegisterState.RegisterInternalError
            NetworkResultType.NoNetworkConnectivity -> RegisterState.RegisterNetworkConnectionError
            NetworkResultType.UsernameAlreadyFound -> RegisterState.RegisterUsernameAlreadyFound
            NetworkResultType.Success -> RegisterState.RegisterSuccess
            else -> RegisterState.RegisterInternalError
        }
    }

}