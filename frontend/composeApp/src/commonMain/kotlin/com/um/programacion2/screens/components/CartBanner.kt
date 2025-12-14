package com.um.programacion2.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.um.programacion2.network.model.EstadoSesion
import com.um.programacion2.network.model.SesionVentaDTO

@Composable
fun CartBanner(
    sesion: SesionVentaDTO,
    onRetomar: () -> Unit
) {
    // Diferenciar estilo si está solo seleccionando o si ya tiene reserva
    val (containerColor, textoPrincipal, icono) = when (sesion.estadoActual) {
        EstadoSesion.CONFIRMANDO -> Triple(
            MaterialTheme.colorScheme.errorContainer,
            "¡Tienes asientos reservados!",
            Icons.Default.Timer
        )
        else -> Triple(
            MaterialTheme.colorScheme.primaryContainer,
            "Compra en proceso",
            Icons.Default.ShoppingCart
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onRetomar() },
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 6.dp,
        color = containerColor
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icono, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = textoPrincipal,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "${sesion.asientosSeleccionados.size} butacas pendientes",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }
            Button(
                onClick = onRetomar,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("VER")
            }
        }
    }
}