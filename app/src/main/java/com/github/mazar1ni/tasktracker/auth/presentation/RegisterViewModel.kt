package com.github.mazar1ni.tasktracker.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mazar1ni.tasktracker.auth.domain.states.RegisterState
import com.github.mazar1ni.tasktracker.auth.domain.use_case.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerUseCase: RegisterUseCase) :
    ViewModel() {

    var username = ""
    var email = ""
    var password = ""
    var confirmPassword = ""

    var stateAction: ((RegisterState) -> Unit)? = null

    fun register() {
        stateAction?.invoke(RegisterState.RegisterInProgress)

        viewModelScope.launch {
            val result = registerUseCase(email, username, password, confirmPassword)
            stateAction?.invoke(result)
        }
    }
}