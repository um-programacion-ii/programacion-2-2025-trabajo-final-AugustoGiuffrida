package com.um.programacion2.screens.tabs

import cafe.adriel.voyager.navigator.tab.Tab
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.um.programacion2.network.services.VentaService
import com.um.programacion2.network.model.EstadoVenta
import com.um.programacion2.network.model.VentaDTO
import com.um.programacion2.screens.venta.DetalleVentaScreen
import kotlinx.coroutines.launch
import kotlin.math.roundToLong

object UserVentasTab: Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.AutoMirrored.Filled.ReceiptLong)
            return remember {
                TabOptions(
                    index = 2u, // Índice 2 (Eventos=0, Perfil=1, Ventas=2)
                    title = "Mis Ventas",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val scope = rememberCoroutineScope()
        val ventaService = remember { VentaService() }
        val parentNavigator = LocalNavigator.currentOrThrow.parent ?: LocalNavigator.currentOrThrow
        var ventas by remember { mutableStateOf<List<VentaDTO>>(emptyList()) }
        var isLoading by remember { mutableStateOf(true) }

        // Cargar ventas al entrar
        LaunchedEffect(Unit) {
            scope.launch {
                isLoading = true
                ventas = ventaService.getUserVentas()
                isLoading = false
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Historial de Compras",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (ventas.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.ConfirmationNumber, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.Gray)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Aún no tienes entradas.", color = Color.Gray)
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(ventas) { venta ->
                        VentaItem(venta){
                            parentNavigator.push(DetalleVentaScreen(venta))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VentaItem(venta: VentaDTO, onClick: () -> Unit) {
    // Definir color según estado
    val (colorEstado, textoEstado) = when {
            venta.resultado -> Color(0xFF4CAF50) to "EXITOSA" // Verde
        venta.estadoVenta == EstadoVenta.CONFIRMADA -> Color(0xFF4CAF50) to "CONFIRMADA"
        venta.estadoVenta == EstadoVenta.RECHAZADA -> Color(0xFFE53935) to "RECHAZADA" // Rojo
        else -> Color(0xFFFF9800) to "PENDIENTE/ERROR" // Naranja
    }

    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { onClick()}
        ) {
            // Cabecera: ID y Estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "#${venta.id}",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )

                Badge(containerColor = colorEstado) {
                    Text(
                        text = textoEstado,
                        modifier = Modifier.padding(4.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Título del Evento
            Text(
                text = venta.evento?.titulo ?: "Evento Desconocido",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Fecha Venta
            Text(
                text = "Fecha: ${venta.fechaVenta?.take(10) ?: "N/A"}",
                style = MaterialTheme.typography.bodySmall
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Precio y Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = venta.descripcion ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
                Text(
                    text = "$${venta.precioVenta?.formatoDinero()}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

private fun Double.formatoDinero(): String {
    val redondeado = (this * 100).roundToLong()
    val enteros = redondeado / 100
    val decimales = redondeado % 100
    return "$enteros.${decimales.toString().padStart(2, '0')}"
}
