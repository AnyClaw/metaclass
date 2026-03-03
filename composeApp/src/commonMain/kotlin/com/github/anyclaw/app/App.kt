package com.github.anyclaw.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.anyclaw.screens.greeting.GreetingScreen
import com.github.anyclaw.screens.login.LoginScreen
import com.github.anyclaw.screens.main.MainScreen

@Composable
fun App() {
    RootNavHost()
}

@Composable
private fun RootNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Destination.Greeting
    ) {
        composable<Destination.Greeting> {
            GreetingScreen(
                onNavigateToLogin = { navController.navigate(Destination.Login) }
            )
        }

        composable<Destination.Login> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Destination.Main) {
                        popUpTo(Destination.Greeting) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<Destination.Main> {
            MainScreen()
        }
    }
}