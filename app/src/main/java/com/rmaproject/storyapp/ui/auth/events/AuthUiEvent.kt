package com.rmaproject.storyapp.ui.auth.events

import com.rmaproject.storyapp.data.remote.model.login.LoginResponse
import com.rmaproject.storyapp.data.remote.model.register.RegisterResponse

sealed class AuthUiEvent {
    data class LoginError(val message: String?) : AuthUiEvent()
    object LoginLoading : AuthUiEvent()
    data class LoginSuccess(val loginResponse: LoginResponse) : AuthUiEvent()
    data class RegisterError(val message: String?) : AuthUiEvent()
    object RegisterLoading : AuthUiEvent()
    data class RegisterSuccess(val registerResponse: RegisterResponse) : AuthUiEvent()
    object ErrSubjectEmpty : AuthUiEvent()
    object ErrEmailInvalid : AuthUiEvent()
    object Static: AuthUiEvent()
}