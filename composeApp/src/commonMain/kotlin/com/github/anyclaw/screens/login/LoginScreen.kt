package com.github.anyclaw.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.anyclaw.dimens.padding_32
import com.github.anyclaw.dimens.padding_64
import metaclass.composeapp.generated.resources.Res
import metaclass.composeapp.generated.resources.enter_login
import metaclass.composeapp.generated.resources.enter_password
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel { LoginViewModel() },
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is LoginUiEvent.LoginSuccessEvent -> onLoginSuccess()
            }
        }
    }

    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = padding_64)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.align(Alignment.Start),
                text = "Логин:"
            )
            TextField(
                modifier = Modifier
                    .padding(bottom = padding_32)
                    .fillMaxWidth(),
                value = state.username,
                onValueChange = viewModel::onUsernameChanged,
                placeholder = { Text(stringResource(Res.string.enter_login)) },
                isError = state.error.isNotBlank()
            )
            Text(
                modifier = Modifier.align(Alignment.Start),
                text = "Пароль:"
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.password,
                onValueChange = viewModel::onPasswordChange,
                placeholder = { Text(stringResource(Res.string.enter_password)) },
                visualTransformation = PasswordVisualTransformation(),
                isError = state.error.isNotBlank()
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = padding_32)
            ) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            Button(
                onClick = viewModel::onLoginClick,
                enabled = state.isLoginButtonActive,
                content = {
                    Text("Войти")
                }
            )
        }
    }
}
