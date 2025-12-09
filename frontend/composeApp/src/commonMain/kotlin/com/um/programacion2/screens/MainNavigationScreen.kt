package com.um.programacion2.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.um.programacion2.network.AuthApiService
class MainNavigationScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val authService = remember { AuthApiService() }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("¡Login Exitoso! Estás en la Pantalla Principal")
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // 1. borrar el token
                    authService.logout()

                    // 2. volver al login
                    navigator.replaceAll(LoginScreen())
                }
            ) {
                Text("Cerrar Sesión")
            }
        }
    }
}