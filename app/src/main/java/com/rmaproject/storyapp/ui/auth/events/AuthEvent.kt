package com.rmaproject.storyapp.ui.auth.events

sealed class AuthEvent {
    data class Login(val email: String, val password: String) : AuthEvent()
    data class Register(val name: String, val email: String, val password: String) : AuthEvent()
}