package com.um.programacion2.screens.venta

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.um.programacion2.network.model.EstadoVenta
import com.um.programacion2.network.model.VentaDTO
import kotlin.math.roundToLong

data class DetalleVentaScreen(val venta: VentaDTO) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scrollState = rememberScrollState()

        // Determinar colores según estado
        val (colorEstado, textoEstado) = when {
            venta.resultado == true -> Color(0xFF4CAF50) to "COMPRA EXITOSA"
            venta.estadoVenta == EstadoVenta.CONFIRMADA -> Color(0xFF4CAF50) to "CONFIRMADA"
            venta.estadoVenta == EstadoVenta.RECHAZADA -> Color(0xFFE53935) to "RECHAZADA"
            else -> Color(0xFFFF9800) to "PENDIENTE"
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Detalle del Ticket") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. Banner de Estado
                Card(
                    colors = CardDefaults.cardColors(containerColor = colorEstado),
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = textoEstado,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 2. Información del Evento
                InfoSection(title = "Evento") {
                    InfoRow(Icons.Default.Event, "Nombre", venta.evento.titulo ?: "Desconocido")
                    if (!venta.evento.direccion.isNullOrBlank()) {
                        InfoRow(Icons.Default.LocationOn, "Lugar", venta.evento.direccion)
                    }
                    InfoRow(Icons.Default.Info, "Descripción", venta.evento.resumen ?: "Sin descripción")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 3. Detalles de la Transacción
                InfoSection(title = "Transacción") {
                    InfoRow(Icons.Default.ConfirmationNumber, "ID Venta", "#${venta.id}")

                    val idCatedraTexto = venta.ventaIdCatedra?.toString() ?: "N/A"
                    InfoRow(Icons.Default.ConfirmationNumber, "ID Cátedra", "#$idCatedraTexto")
                    InfoRow(Icons.Default.DateRange, "Fecha Compra", venta.fechaVenta.take(10))
                    InfoRow(Icons.Default.Info, "Estado", venta.descripcion)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 4. Total Pagado (Destacado)
                Card(
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total Pagado", style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = "$${venta.precioVenta.formatoDinero()}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

// Componentes auxiliares para ordenar el código
@Composable
fun InfoSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            content()
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(icon, null, modifier = Modifier.size(20.dp), tint = Color.Gray)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Text(value, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

private fun Double.formatoDinero(): String {
    val redondeado = (this * 100).roundToLong()
    val enteros = redondeado / 100
    val decimales = redondeado % 100
    return "$enteros.${decimales.toString().padStart(2, '0')}"
}