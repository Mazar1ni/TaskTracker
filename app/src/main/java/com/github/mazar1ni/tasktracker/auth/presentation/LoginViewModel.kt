package com.github.mazar1ni.tasktracker.auth.presentation

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mazar1ni.tasktracker.auth.domain.states.LoginState
import com.github.mazar1ni.tasktracker.auth.domain.use_case.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {

    private var username = ""
    private var password = ""

    val usernameWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(text: Editable?) {
            username = text.toString()
        }
    }

    val passwordWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(text: Editable?) {
            password = text.toString()
        }
    }

    var stateAction: ((LoginState) -> Unit)? = null

    fun signIn() {
        stateAction?.invoke(LoginState.LoginInProgress)

        viewModelScope.launch {
            val result = loginUseCase(username, password)
            stateAction?.invoke(result)
        }
    }
}