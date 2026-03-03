package com.github.anyclaw.screens.login

import androidx.compose.runtime.Immutable

@Immutable
data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoginButtonActive: Boolean = false,
    val error: String = "",
)
