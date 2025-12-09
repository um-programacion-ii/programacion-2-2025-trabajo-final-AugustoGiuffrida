package com.um.programacion2.screens

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.um.programacion2.network.AuthApiService
import com.um.programacion2.screens.LoginScreen
class MainNavigationScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val authService = remember { AuthApiService() }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("¡Login Exitoso! Estás en la Pantalla Principal")
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // 1. Borramos el token
                    authService.logout()

                    // 2. Volvemos al login reemplazando la pantalla actual
                    navigator.replaceAll(LoginScreen())
                }
            ) {
                Text("Cerrar Sesión")
            }
        }
    }
}