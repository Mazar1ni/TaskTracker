package com.github.mazar1ni.tasktracker.auth.presentation

import android.text.Editable
import android.text.TextWatcher
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

    private var username = ""
    private var email = ""
    private var password = ""
    private var confirmPassword = ""

    val usernameWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(text: Editable?) {
            username = text.toString()
        }
    }

    val emailWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(text: Editable?) {
            email = text.toString()
        }
    }

    val passwordWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(text: Editable?) {
            password = text.toString()
        }
    }

    val confirmPasswordWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(text: Editable?) {
            confirmPassword = text.toString()
        }
    }

    var stateAction: ((RegisterState) -> Unit)? = null

    fun register() {
        stateAction?.invoke(RegisterState.RegisterInProgress)

        viewModelScope.launch {
            val result = registerUseCase(email, username, password, confirmPassword)
            stateAction?.invoke(result)
        }
    }
}