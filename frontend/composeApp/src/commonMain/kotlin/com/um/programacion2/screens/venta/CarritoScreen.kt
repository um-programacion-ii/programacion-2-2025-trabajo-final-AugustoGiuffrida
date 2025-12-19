package com.um.programacion2.screens.venta

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.um.programacion2.network.model.ConfirmarCompraDTO
import com.um.programacion2.network.model.DetalleAsientoCompra
import com.um.programacion2.network.model.EventoDTO
import com.um.programacion2.network.services.VentaService
import kotlinx.coroutines.launch
import kotlin.math.roundToLong

data class CarritoScreen(
    val evento: EventoDTO,
    val compraDTO: ConfirmarCompraDTO
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        val ventaService = remember { VentaService() }
        var isPaying by remember { mutableStateOf(false) }

        // Calculamos total
        val total = (evento.precioEntrada ?: 0.0) * compraDTO.detalles.size

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = { Text("Resumen de Compra") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                )
            },
            bottomBar = {
                Surface(tonalElevation = 8.dp, shadowElevation = 8.dp) {
                    Column(Modifier.padding(16.dp)) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total a Pagar:", style = MaterialTheme.typography.titleMedium)
                            Text(
                                "$${total.formatoDinero()}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = {
                                scope.launch {
                                    isPaying = true
                                    val resultado = ventaService.comprar(compraDTO)

                                    if (resultado != null && resultado.resultado == true) {
                                        // Éxito: Reemplazar todo el stack con el ticket
                                        navigator.replaceAll(DetalleVentaScreen(resultado))
                                    } else {
                                        val error = resultado?.descripcion ?: "Error al procesar el pago"
                                        snackbarHostState.showSnackbar(error)
                                    }
                                    isPaying = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isPaying
                        ) {
                            if (isPaying) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                            } else {
                                Icon(Icons.Default.CreditCard, null)
                                Spacer(Modifier.width(8.dp))
                                Text("Pagar Ahora")
                            }
                        }
                    }
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Banner de Reserva
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Timer, null, tint = Color(0xFF1976D2))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "¡Asientos Reservados! Tienes 5 minutos para completar tu compra.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF0D47A1)
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                Text(
                    evento.titulo ?: "Evento",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(evento.direccion ?: "", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)

                HorizontalDivider(
                    Modifier.padding(vertical = 16.dp),
                    DividerDefaults.Thickness,
                    DividerDefaults.color
                )

                Text("Items:", style = MaterialTheme.typography.titleMedium)

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(compraDTO.detalles) { detalle ->
                        CartItem(detalle, evento.precioEntrada ?: 0.0)
                    }
                }
            }
        }
    }
}

@Composable
fun CartItem(detalle: DetalleAsientoCompra, precio: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                "Asiento Fila ${detalle.fila} / Col ${detalle.columna}",
                fontWeight = FontWeight.Bold
            )
            Text(
                detalle.nombrePersona,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
        Text("$${precio.formatoDinero()}")
    }
}

private fun Double.formatoDinero(): String {
    val redondeado = (this * 100).roundToLong()
    val enteros = redondeado / 100
    val decimales = redondeado % 100
    return "$enteros.${decimales.toString().padStart(2, '0')}"
}