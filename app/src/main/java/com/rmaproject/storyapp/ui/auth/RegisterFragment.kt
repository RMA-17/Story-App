package com.rmaproject.storyapp.ui.auth

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.storyapp.R
import com.rmaproject.storyapp.data.factory.ViewModelFactory
import com.rmaproject.storyapp.databinding.FragmentRegisterBinding
import com.rmaproject.storyapp.ui.auth.events.AuthEvent
import com.rmaproject.storyapp.ui.auth.events.AuthUiEvent
import com.rmaproject.storyapp.utils.showSnackbar

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val binding: FragmentRegisterBinding by viewBinding()
    private val viewModel: AuthViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.editText?.text.toString()
            viewModel.onEvent(AuthEvent.Register(name, email, password))
        }

        viewModel.eventFLow.observe(viewLifecycleOwner) { event ->
            when (event) {
                is AuthUiEvent.ErrEmailInvalid -> {
                    showSnackbar(
                        binding.root, getString(R.string.err_email_not_valid)
                    ).show()
                }
                is AuthUiEvent.ErrSubjectEmpty -> {
                    showSnackbar(
                        binding.root, getString(R.string.err_login_empty)
                    ).show()
                }
                is AuthUiEvent.RegisterError -> {
                    showSnackbar(
                        binding.root,
                        event.message.toString()
                    ).show()
                    toggleButtonAndProgress(true)
                }
                is AuthUiEvent.RegisterLoading -> {
                    toggleButtonAndProgress(false)
                }
                is AuthUiEvent.RegisterSuccess -> {
                    Toast.makeText(
                        requireContext(),
                        event.registerResponse.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    toggleButtonAndProgress(true)
                }
                is AuthUiEvent.Static -> {

                }
                else -> throw Exception("Unknown Event")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = TransitionInflater.from(requireContext())

        exitTransition = inflater.inflateTransition(android.R.transition.slide_right)
        enterTransition = inflater.inflateTransition(android.R.transition.slide_left)
    }

    private fun toggleButtonAndProgress(isEnabled: Boolean) {
        binding.btnRegister.isEnabled = isEnabled
        binding.btnLogin.isEnabled = isEnabled
        binding.progressCircular.isVisible = !isEnabled
    }
}