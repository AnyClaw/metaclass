package com.github.anyclaw.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.anyclaw.dimens.padding_32
import com.github.anyclaw.dimens.padding_64

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(padding_64)
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
                state = rememberTextFieldState(),
                placeholder = { Text("Введите логин") },
            )
            Text(
                modifier = Modifier.align(Alignment.Start),
                text = "Пароль:"
            )
            TextField(
                modifier = Modifier
                    .padding(bottom = padding_32)
                    .fillMaxWidth(),
                state = rememberTextFieldState(),
                placeholder = { Text("Введите пароль") }
            )
            Button(
                onClick = {},
                content = {
                    Text("Войти")
                }
            )
        }
    }
}
