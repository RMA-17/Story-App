package com.rmaproject.storyapp.ui.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmaproject.storyapp.data.repository.StoryRepository
import com.rmaproject.storyapp.ui.auth.events.AuthEvent
import com.rmaproject.storyapp.ui.auth.events.AuthUiEvent
import com.rmaproject.storyapp.utils.isValidEmail
import com.rmaproject.storyapp.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.launch

open class AuthViewModel(
    private val repository: StoryRepository,
) : ViewModel() {

    private val _eventFLow = MutableLiveData<AuthUiEvent>()
    val eventFLow: LiveData<AuthUiEvent> = _eventFLow

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.Login -> {
                viewModelScope.launch {
                    if (event.email.isEmpty() || event.password.isEmpty()) {
                        _eventFLow.postValue(AuthUiEvent.ErrSubjectEmpty)
                        return@launch
                    }
                    if (!event.email.isValidEmail()) {
                        _eventFLow.postValue(AuthUiEvent.ErrEmailInvalid)
                        return@launch
                    }
                    _eventFLow.postValue(AuthUiEvent.LoginLoading)
                    wrapEspressoIdlingResource {
                        try {
                            val response = repository.loginUser(event.email, event.password)
                            if (response.error) {
                                _eventFLow.postValue(AuthUiEvent.LoginError(response.message))
                                return@launch
                            }
                            _eventFLow.postValue(AuthUiEvent.LoginSuccess(response))
                        } catch (e: Exception) {
                            Log.d("LOG_ERR", e.toString())
                            _eventFLow.postValue(
                                AuthUiEvent.LoginError(
                                    e.message ?: "There's an error when signing in"
                                )
                            )
                        }
                    }
                }
            }
            is AuthEvent.Register -> {
                viewModelScope.launch {
                    if (event.email.isEmpty() || event.password.isEmpty() || event.name.isEmpty()) {
                        _eventFLow.postValue(AuthUiEvent.ErrSubjectEmpty)
                        return@launch
                    }
                    if (!event.email.isValidEmail()) {
                        _eventFLow.postValue(AuthUiEvent.ErrEmailInvalid)
                        return@launch
                    }
                    _eventFLow.postValue(AuthUiEvent.RegisterLoading)
                    wrapEspressoIdlingResource {
                        try {
                            val response =
                                repository.registerUser(event.name, event.email, event.password)
                            if (response.error) {
                                _eventFLow.postValue(AuthUiEvent.RegisterError(response.message))
                                return@launch
                            }
                            _eventFLow.postValue(AuthUiEvent.RegisterSuccess(response))
                        } catch (e: Exception) {
                            Log.d("REG_ERR", e.toString())
                            _eventFLow.postValue(
                                AuthUiEvent.RegisterError(
                                    e.message ?: "There's an error when signing in"
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}