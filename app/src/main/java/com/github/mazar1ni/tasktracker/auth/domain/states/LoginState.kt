package com.github.mazar1ni.tasktracker.auth.domain.states

sealed class LoginState {
    object LoginInProgress : LoginState()

    object LoginNetworkConnectionError : LoginState()
    object LoginInternalError : LoginState()
    object LoginIncorrectCredentials : LoginState()

    object LoginUsernameLength : LoginState()
    object LoginPasswordLength : LoginState()

    object LoginSuccess : LoginState()
}
