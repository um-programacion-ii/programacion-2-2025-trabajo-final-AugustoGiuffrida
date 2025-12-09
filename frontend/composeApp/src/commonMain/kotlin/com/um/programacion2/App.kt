package com.um.programacion2


import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import com.um.programacion2.network.AuthApiService
import com.um.programacion2.screens.LoginScreen
import com.um.programacion2.screens.MainNavigationScreen

@Composable
fun App() {
    MaterialTheme {
        val authService = remember { AuthApiService() }
        val isLoggedIn = authService.isLoggedIn()

        Navigator(
            screen = if (isLoggedIn) MainNavigationScreen() else LoginScreen()
        )
    }
}