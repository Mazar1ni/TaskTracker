package com.github.mazar1ni.tasktracker.auth.presentation

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mazar1ni.tasktracker.R
import com.github.mazar1ni.tasktracker.auth.domain.states.LoginState
import com.github.mazar1ni.tasktracker.auth.domain.use_case.LoginUseCase
import com.github.mazar1ni.tasktracker.core.util.DialogUtil
import com.github.mazar1ni.tasktracker.core.util.NavigationUtil
import com.github.mazar1ni.tasktracker.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    private var progressDialog: Dialog? = null

    @Inject
    lateinit var navigationUtil: NavigationUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)

        binding.usernameEditText.addTextChangedListener(viewModel.usernameWatcher)
        binding.passwordEditText.addTextChangedListener(viewModel.passwordWatcher)

        binding.signIn.setOnClickListener { viewModel.signIn() }
        binding.forgotPassword.setOnClickListener {
            view?.let {
                Snackbar.make(it, R.string.feature_does_not_support, Snackbar.LENGTH_SHORT).show()
            }
        }
        binding.signUp.setOnClickListener {
            navigationUtil.navigate(LoginFragmentDirections.actionFragmentLoginToFragmentRegister())
        }

        viewModel.stateAction = { state ->
            clearErrorFields()

            when (state) {
                LoginState.LoginInProgress -> {
                    progressDialog = DialogUtil.showProgressDialog(requireContext())
                }
                LoginState.LoginUsernameLength -> {
                    progressDialog?.dismiss()

                    binding.usernameField.error = requireContext().getString(
                        R.string.username_length_error,
                        LoginUseCase.MIN_CREDENTIAL_LENGTH,
                        LoginUseCase.MAX_CREDENTIAL_LENGTH
                    )
                }
                LoginState.LoginPasswordLength -> {
                    progressDialog?.dismiss()

                    binding.passwordField.error = requireContext().getString(
                        R.string.password_length_error,
                        LoginUseCase.MIN_CREDENTIAL_LENGTH,
                        LoginUseCase.MAX_CREDENTIAL_LENGTH
                    )
                }
                LoginState.LoginIncorrectCredentials -> {
                    progressDialog?.dismiss()

                    binding.passwordField.error =
                        requireContext().getString(R.string.credentials_error)
                }
                LoginState.LoginNetworkConnectionError -> {
                    progressDialog?.dismiss()

                    view?.let {
                        Snackbar.make(it, R.string.no_network_error, Snackbar.LENGTH_SHORT).show()
                    }
                }
                LoginState.LoginInternalError -> {
                    progressDialog?.dismiss()

                    view?.let {
                        Snackbar.make(it, R.string.internal_error, Snackbar.LENGTH_SHORT).show()
                    }
                }
                LoginState.LoginSuccess -> {
                    progressDialog?.dismiss()
                    navigationUtil.navigate(LoginFragmentDirections.actionFragmentLoginToNavigationTasks())
                }
            }
        }

        return binding.root
    }

    private fun clearErrorFields() {
        binding.usernameField.error = null
        binding.passwordField.error = null
    }

}