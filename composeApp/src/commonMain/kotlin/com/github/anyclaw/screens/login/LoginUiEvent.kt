package com.github.anyclaw.screens.login

sealed class LoginUiEvent {
    object LoginSuccessEvent : LoginUiEvent()
}