package com.github.anyclaw.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImage
import com.github.anyclaw.dimens.padding_32
import com.github.anyclaw.urls.greetingImage
import metaclass.composeapp.generated.resources.Res
import metaclass.composeapp.generated.resources.welcome
import org.jetbrains.compose.resources.painterResource

@Composable
fun GreetingScreen(
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
                .padding(padding_32)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f),
                model = greetingImage,
                contentDescription = null,
                error = painterResource(Res.drawable.welcome)
            )
            Box(
                modifier = Modifier.weight(0.5f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier,
                    text = "Welcome to my first project on KMP and CMP!"
                )
            }
            Button(
                modifier = Modifier,
                onClick = onNavigateToLogin,
                content = {
                    Text("Go to login page")
                }
            )
        }
    }
}
