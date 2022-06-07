package com.github.mazar1ni.tasktracker.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mazar1ni.tasktracker.auth.domain.states.LoginState
import com.github.mazar1ni.tasktracker.auth.domain.use_case.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {

    var username = ""
    var password = ""

    var stateAction: ((LoginState) -> Unit)? = null

    fun signIn() {
        stateAction?.invoke(LoginState.LoginInProgress)

        viewModelScope.launch {
            val result = loginUseCase(username, password)
            stateAction?.invoke(result)
        }
    }
}