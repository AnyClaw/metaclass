package com.github.anyclaw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.anyclaw.routes.greetingRoute
import com.github.anyclaw.routes.loginRoute
import com.github.anyclaw.screens.GreetingScreen
import com.github.anyclaw.screens.LoginScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = greetingRoute
            ){
                composable(greetingRoute) {
                    GreetingScreen({
                        navController.graph.findNode(loginRoute)?.id?.let { resId ->
                            navController.navigate(
                                resId = resId,
                            )
                        }
                    })
                }

                composable(loginRoute) {
                    LoginScreen()
                }
            }
        }
    }
}

