package com.rmaproject.storyapp.ui.auth

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.storyapp.R
import com.rmaproject.storyapp.data.factory.ViewModelFactory
import com.rmaproject.storyapp.data.preferences.UserInfo
import com.rmaproject.storyapp.databinding.FragmentLoginBinding
import com.rmaproject.storyapp.ui.auth.events.AuthEvent
import com.rmaproject.storyapp.ui.auth.events.AuthUiEvent
import com.rmaproject.storyapp.utils.showSnackbar

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding: FragmentLoginBinding by viewBinding()
    private val viewModel: AuthViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_nav_login_to_nav_register)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            viewModel.onEvent(AuthEvent.Login(email, password))
        }

        viewModel.eventFLow.observe(viewLifecycleOwner) { event ->
            when (event) {
                is AuthUiEvent.LoginError -> {
                    showSnackbar(
                        binding.root,
                        event.message.toString()
                    ).show()
                    toggleButtonAndProgress(true)
                }
                is AuthUiEvent.LoginLoading -> {
                    toggleButtonAndProgress(false)
                }
                is AuthUiEvent.LoginSuccess -> {
                    val (name, token, userId) = event.loginResponse.loginResult
                    UserInfo.apply {
                        this.name = name.toString()
                        this.token = token.toString()
                        this.userId = userId.toString()
                    }
                    toggleButtonAndProgress(true)
                    findNavController().navigate(R.id.action_nav_login_to_nav_story_list)
                }
                is AuthUiEvent.ErrSubjectEmpty -> {
                    showSnackbar(
                        binding.root,
                        getString(R.string.err_login_empty)
                    ).show()
                }
                is AuthUiEvent.ErrEmailInvalid -> {
                    showSnackbar(
                        binding.root,
                        getString(R.string.err_email_not_valid)
                    ).show()
                }
                else -> throw Exception("Unknown Event")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(android.R.transition.slide_left)
        reenterTransition = inflater.inflateTransition(android.R.transition.slide_right)
    }

    private fun toggleButtonAndProgress(isEnabled: Boolean) {
        binding.progressIndicator.isVisible = !isEnabled
        binding.btnLogin.isEnabled = isEnabled
        binding.btnRegister.isEnabled = isEnabled
    }
}