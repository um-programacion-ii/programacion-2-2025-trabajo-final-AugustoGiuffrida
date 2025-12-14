package com.um.programacion2.screens

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.um.programacion2.network.model.SesionVentaDTO
import com.um.programacion2.screens.tabs.EventosTab
import com.um.programacion2.screens.tabs.PerfilTab
import com.um.programacion2.screens.tabs.UserVentasTab
import com.um.programacion2.network.model.AsientoDTO
import com.um.programacion2.network.model.ConfirmarCompraDTO
import com.um.programacion2.network.model.DetalleAsientoCompra
import com.um.programacion2.network.model.EstadoAsientoUI
import com.um.programacion2.network.model.EstadoSesion
import com.um.programacion2.network.services.AccountService
import com.um.programacion2.network.services.EventoService
import com.um.programacion2.network.services.VentaService
import com.um.programacion2.screens.components.CartBanner
import com.um.programacion2.screens.venta.CargaDatosScreen
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import com.um.programacion2.screens.venta.CarritoScreen

class MainNavigationScreen : Screen {

    @Composable
    override fun Content() {
        val scope = rememberCoroutineScope()
        val rootNavigator = LocalNavigator.currentOrThrow
        val eventoService = remember { EventoService() }
        val ventaService = remember { VentaService() }
        val accountService = remember { AccountService() }
        var sesionActiva by remember { mutableStateOf<SesionVentaDTO?>(null) }


        LaunchedEffect(Unit) {
            while (isActive) { // Mientras la pantalla esté viva
                try {
                    // Consultamos a Redis si hay algo en el carrito
                    val nuevaSesion = ventaService.getSesionActual()

                    // Solo actualizamos si es diferente para evitar recomposiciones innecesarias
                    if (sesionActiva != nuevaSesion) {
                        sesionActiva = nuevaSesion
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                delay(2000)
            }
        }

        TabNavigator(EventosTab) {
            Scaffold(
                bottomBar = {
                    Column {
                        // --- LÓGICA DEL BANNER INTELIGENTE ---
                        sesionActiva?.let { sesion ->
                            if (sesion.asientosSeleccionados.isNotEmpty() && sesion.estadoActual != EstadoSesion.FINALIZADO) {
                                CartBanner(sesion = sesion) {
                                    scope.launch {
                                        try {
                                            // 1. Recuperar datos del evento (siempre necesario)
                                            val evento = eventoService.getEventoById(sesion.eventoId)

                                            if (evento != null) {

                                                // --- DECISIÓN DE NAVEGACIÓN ---
                                                if (sesion.estadoActual == EstadoSesion.CONFIRMANDO) {
                                                    // CASO A: ESTA BLOQUEADO -> IR A PAGAR (CARRITO)

                                                    // Recuperamos usuario para poner el nombre por defecto
                                                    val account = accountService.getAccount()
                                                    val nombreUsuario = if (account != null) {
                                                        "${account.firstName ?: ""} ${account.lastName ?: ""}".trim().ifEmpty { account.login }
                                                    } else {
                                                        "Usuario"
                                                    }

                                                    // Construimos el DTO de Compra directo
                                                    val detalles = sesion.asientosSeleccionados.map { asiento ->
                                                        DetalleAsientoCompra(
                                                            fila = asiento.fila,
                                                            columna = asiento.columna,
                                                            nombrePersona = nombreUsuario
                                                        )
                                                    }
                                                    val compraDTO =
                                                        ConfirmarCompraDTO(detalles = detalles)

                                                    // Navegar directo al Carrito
                                                    rootNavigator.push(CarritoScreen(evento, compraDTO))

                                                } else {
                                                    // CASO B: SELECCIONADO -> IR A CONFIRMAR (CARGA DATOS)

                                                    val asientosUI = sesion.asientosSeleccionados.map {
                                                        AsientoDTO(it.fila, it.columna, EstadoAsientoUI.SELECCIONADO)
                                                    }

                                                    rootNavigator.push(CargaDatosScreen(evento, asientosUI))
                                                }

                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    }
                                }
                            }
                        }

                    NavigationBar {
                        // Agregar items barra inferior
                        TabNavigationItem(EventosTab)
                        TabNavigationItem(UserVentasTab)
                        TabNavigationItem(PerfilTab)
                    }
                }
                }
            ) { innerPadding ->
                // Renderizar contenido del Tab actual
                Box(modifier = Modifier.padding(innerPadding)) {
                    CurrentTab()
                }
            }
        }
    }
}

    @Composable
    private fun RowScope.TabNavigationItem(tab: Tab) {
        val tabNavigator = LocalTabNavigator.current

        NavigationBarItem(
            selected = tabNavigator.current == tab,
            onClick = { tabNavigator.current = tab },
            icon = {
                tab.options.icon?.let { icon ->
                    Icon(painter = icon, contentDescription = tab.options.title)
                }
            },
            label = {
                Text(text = tab.options.title)
            }
        )
    }
