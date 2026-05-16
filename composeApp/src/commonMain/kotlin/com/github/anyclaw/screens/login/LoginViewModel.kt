package com.github.anyclaw.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    val incorrectLoginOrPasswordMessage = "Неверный логин или пароль"
    val mockUsername = "user"
    val mockPassword = "password"

    private val mutableState = MutableStateFlow(LoginUiState())

    val state: StateFlow<LoginUiState>
        get() = mutableState

    private val _events = MutableSharedFlow<LoginUiEvent>()
    val events: SharedFlow<LoginUiEvent> = _events.asSharedFlow()

    fun onUsernameChanged(newUsername: String) {
        mutableState.update {
            it.copy(
                username = newUsername,
                error = "",
                isLoginButtonActive = newUsername.isNotBlank() && it.password.isNotBlank()
            )
        }
    }

    fun onPasswordChange(newPassword: String) {
        mutableState.update {
            it.copy(
                password = newPassword,
                error = "",
                isLoginButtonActive = newPassword.isNotBlank() && it.username.isNotBlank()
            )
        }
    }

    fun onLoginClick() {
        val currentState = mutableState.value

        val isValid = currentState.username == mockUsername && currentState.password == mockPassword

        if (isValid) {
            viewModelScope.launch {
                _events.emit(LoginUiEvent.LoginSuccessEvent)
            }
        }
        else {
            mutableState.update {
                it.copy(
                    error = incorrectLoginOrPasswordMessage,
                    isLoginButtonActive = false
                )
            }
        }
    }
}