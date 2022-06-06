package com.github.mazar1ni.tasktracker.auth.presentation

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mazar1ni.tasktracker.R
import com.github.mazar1ni.tasktracker.auth.domain.states.RegisterState
import com.github.mazar1ni.tasktracker.auth.domain.use_case.LoginUseCase
import com.github.mazar1ni.tasktracker.core.util.DialogUtil
import com.github.mazar1ni.tasktracker.core.util.NavigationUtil
import com.github.mazar1ni.tasktracker.databinding.FragmentRegisterBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

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
        binding = FragmentRegisterBinding.inflate(layoutInflater)

        binding.usernameEditText.addTextChangedListener(viewModel.usernameWatcher)
        binding.emailEditText.addTextChangedListener(viewModel.emailWatcher)
        binding.passwordEditText.addTextChangedListener(viewModel.passwordWatcher)
        binding.confirmPasswordEditText.addTextChangedListener(viewModel.confirmPasswordWatcher)

        binding.signUp.setOnClickListener { viewModel.register() }

        viewModel.stateAction = { state ->
            clearErrorFields()

            when (state) {
                RegisterState.RegisterInProgress -> {
                    progressDialog = DialogUtil.showProgressDialog(requireContext())
                }
                RegisterState.RegisterUsernameLength -> {
                    progressDialog?.dismiss()

                    binding.usernameField.error = requireContext().getString(
                        R.string.username_length_error,
                        LoginUseCase.MIN_CREDENTIAL_LENGTH,
                        LoginUseCase.MAX_CREDENTIAL_LENGTH
                    )
                }
                RegisterState.RegisterPasswordLength -> {
                    progressDialog?.dismiss()

                    binding.passwordField.error = requireContext().getString(
                        R.string.password_length_error,
                        LoginUseCase.MIN_CREDENTIAL_LENGTH,
                        LoginUseCase.MAX_CREDENTIAL_LENGTH
                    )
                }
                RegisterState.RegisterConfirmPasswordLength -> {
                    progressDialog?.dismiss()

                    binding.confirmPasswordField.error = requireContext().getString(
                        R.string.password_length_error,
                        LoginUseCase.MIN_CREDENTIAL_LENGTH,
                        LoginUseCase.MAX_CREDENTIAL_LENGTH
                    )
                }
                RegisterState.RegisterPasswordsDoNotMatch -> {
                    progressDialog?.dismiss()

                    binding.confirmPasswordField.error =
                        requireContext().getString(R.string.passwords_do_not_match)
                }
                RegisterState.RegisterEmailIncorrect -> {
                    progressDialog?.dismiss()

                    binding.emailField.error = requireContext().getString(R.string.email_incorrect)
                }
                RegisterState.RegisterUsernameAlreadyFound -> {
                    progressDialog?.dismiss()

                    view?.let {
                        Snackbar.make(it, R.string.username_already_found, Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
                RegisterState.RegisterNetworkConnectionError -> {
                    progressDialog?.dismiss()

                    view?.let {
                        Snackbar.make(it, R.string.no_network_error, Snackbar.LENGTH_SHORT).show()
                    }
                }
                RegisterState.RegisterInternalError -> {
                    progressDialog?.dismiss()

                    view?.let {
                        Snackbar.make(it, R.string.internal_error, Snackbar.LENGTH_SHORT).show()
                    }
                }
                RegisterState.RegisterSuccess -> {
                    progressDialog?.dismiss()
                    navigationUtil.navigate(RegisterFragmentDirections.actionFragmentRegisterToNavigationTasks())
                }
            }
        }

        return binding.root
    }

    private fun clearErrorFields() {
        binding.usernameField.error = null
        binding.emailField.error = null
        binding.passwordField.error = null
        binding.confirmPasswordField.error = null
    }
}