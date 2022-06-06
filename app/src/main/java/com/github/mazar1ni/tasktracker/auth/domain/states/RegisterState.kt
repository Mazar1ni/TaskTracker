package com.github.mazar1ni.tasktracker.auth.domain.states

sealed class RegisterState {
    object RegisterInProgress : RegisterState()

    object RegisterNetworkConnectionError : RegisterState()
    object RegisterInternalError : RegisterState()
    object RegisterUsernameAlreadyFound : RegisterState()

    object RegisterUsernameLength : RegisterState()
    object RegisterPasswordLength : RegisterState()
    object RegisterConfirmPasswordLength : RegisterState()
    object RegisterPasswordsDoNotMatch : RegisterState()
    object RegisterEmailIncorrect : RegisterState()

    object RegisterSuccess : RegisterState()
}
