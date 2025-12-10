package com.um.programacion2.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import kotlin.math.roundToLong
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.um.programacion2.network.AsientoService
import com.um.programacion2.network.model.AsientoDTO
import com.um.programacion2.network.model.EventoDTO
import com.um.programacion2.network.model.EstadoAsientoUI

data class DetalleEventoScreen(val evento: EventoDTO) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val screenModel = rememberScreenModel {
            DetalleEventoScreenModel(AsientoService(), evento)
        }

        val state by screenModel.state.collectAsState()
        var scale by remember { mutableStateOf(1f) }
        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(evento.titulo, maxLines = 1) },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    },
                    actions = {
                        // Controles de zoom
                        IconButton(onClick = {
                            scale = (scale - 0.2f).coerceAtLeast(0.5f)
                        }) {
                            Icon(Icons.Default.ZoomOut, contentDescription = "Alejar")
                        }
                        IconButton(onClick = {
                            scale = (scale + 0.2f).coerceAtMost(3f)
                        }) {
                            Icon(Icons.Default.ZoomIn, contentDescription = "Acercar")
                        }
                    }
                )
            },
            bottomBar = {
                if (state.seleccionados.isNotEmpty()) {
                    BottomBarCompra(
                        cantidad = state.seleccionados.size,
                        total = (state.seleccionados.size * evento.precioEntrada),
                        onContinuar = {
                            // TODO: Navegar a pantalla de confirmación
                        }
                    )
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                // Info del Evento - Colapsable
                var expanded by remember { mutableStateOf(false) }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { expanded = !expanded }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = evento.descripcion ?: "Sin descripción",
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = if (expanded) Int.MAX_VALUE else 2
                        )
                        if (!expanded) {
                            Text(
                                "Toca para ver más",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                HorizontalDivider()

                // Referencias de colores - Compactas
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ReferenciaItem(Color(0xFF4CAF50), "Libre")
                    ReferenciaItem(Color(0xFFE53935), "Ocupado")
                    ReferenciaItem(Color(0xFFFDD835), "Tu Selección")
                }

                HorizontalDivider()

                // Indicador de zoom
                Text(
                    text = "Zoom: ${(scale * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(8.dp)
                )

                // MAPA DE ASIENTOS CON ZOOM Y PAN
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF5F5F5))
                        .clipToBounds()
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    } else {
                        MapaAsientosMejorado(
                            asientos = state.asientos,
                            seleccionados = state.seleccionados,
                            onAsientoClick = { screenModel.toggleAsiento(it) },
                            scale = scale,
                            offsetX = offsetX,
                            offsetY = offsetY,
                            onScaleChange = { newScale -> scale = newScale.coerceIn(0.5f, 2.5f) },
                            onOffsetChange = { x, y ->
                                offsetX = x
                                offsetY = y
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MapaAsientosMejorado(
    asientos: List<AsientoDTO>,
    seleccionados: Set<AsientoDTO>,
    onAsientoClick: (AsientoDTO) -> Unit,
    scale: Float,
    offsetX: Float,
    offsetY: Float,
    onScaleChange: (Float) -> Unit,
    onOffsetChange: (Float, Float) -> Unit
) {
    val verticalScroll = rememberScrollState()
    val horizontalScroll = rememberScrollState()

    // Agrupamos por fila
    val filas = remember(asientos) {
        asientos.groupBy { it.fila }.entries.sortedBy { it.key }
    }

    // Tamaño base más pequeño para matrices grandes
    val baseSize = 28.dp
    val spacing = 4.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    onScaleChange(scale * zoom)
                    onOffsetChange(offsetX + pan.x, offsetY + pan.y)
                }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(verticalScroll)
                .horizontalScroll(horizontalScroll)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offsetX,
                    translationY = offsetY
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Escenario visual mejorado
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(30.dp)
                    .background(
                        Color(0xFF424242),
                        RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "ESCENARIO",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Renderizar filas con pasillos
            filas.forEachIndexed { index, (_, asientosEnFila) ->
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(vertical = spacing / 2)
                ) {
                    // Número de fila
                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .height(baseSize),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${asientosEnFila.first().fila}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.width(spacing))

                    // Asientos ordenados por columna
                    asientosEnFila.sortedBy { it.columna }.forEachIndexed { colIndex, asiento ->
                        val esSeleccionado = seleccionados.contains(asiento)
                        val esOcupado = asiento.estado == EstadoAsientoUI.OCUPADO

                        AsientoItemMejorado(
                            asiento = asiento,
                            esOcupado = esOcupado,
                            esSeleccionado = esSeleccionado,
                            onClick = { onAsientoClick(asiento) },
                            size = baseSize
                        )

                        // Pasillo en el medio (cada 5 asientos)
                        if ((colIndex + 1) % 5 == 0 && colIndex < asientosEnFila.size - 1) {
                            Spacer(modifier = Modifier.width(spacing * 2))
                        } else {
                            Spacer(modifier = Modifier.width(spacing))
                        }
                    }
                }

                // Pasillo horizontal cada 3 filas
                if ((index + 1) % 3 == 0 && index < filas.size - 1) {
                    Spacer(modifier = Modifier.height(spacing * 2))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}



@Composable
fun AsientoItemMejorado(
    asiento: AsientoDTO,
    esOcupado: Boolean,
    esSeleccionado: Boolean,
    onClick: () -> Unit,
    size: androidx.compose.ui.unit.Dp = 28.dp
) {
    val color = when {
        esOcupado -> Color(0xFFE53935) // Rojo más suave
        esSeleccionado -> Color(0xFFFDD835) // Amarillo vibrante
        else -> Color(0xFF4CAF50) // Verde suave
    }

    val borderColor = when {
        esSeleccionado -> Color(0xFFF57F17) // Borde dorado para seleccionados
        else -> color.copy(alpha = 0.3f)
    }

    Box(
        modifier = Modifier
            .size(size)
            .background(color, RoundedCornerShape(4.dp))
            .border(
                width = if (esSeleccionado) 2.dp else 0.5.dp,
                color = borderColor,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable(enabled = !esOcupado) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${asiento.columna}",
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = MaterialTheme.typography.labelSmall.fontSize * 0.8
            ),
            color = if (esOcupado) Color.White else Color.Black,
            fontWeight = if (esSeleccionado) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun ReferenciaItem(color: Color, texto: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, RoundedCornerShape(2.dp))
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            texto,
            style = MaterialTheme.typography.labelSmall,
            fontSize = MaterialTheme.typography.labelSmall.fontSize * 0.9
        )
    }
}

@Composable
fun BottomBarCompra(cantidad: Int, total: Double, onContinuar: () -> Unit) {
    Surface(
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "$cantidad asiento${if (cantidad != 1) "s" else ""} seleccionado${if (cantidad != 1) "s" else ""}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "Total: $${total.formatoDinero()}",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                if (cantidad >= 4) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Máximo 4 asientos por compra",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Button(
                onClick = onContinuar,
                enabled = cantidad in 1..4
            ) {
                Text("Continuar")
            }
        }
    }
}

private fun Double.formatoDinero(): String {
    val redondeado = (this * 100).roundToLong()
    val enteros = redondeado / 100
    val decimales = redondeado % 100
    // padStart asegura que 5 se convierta en "05"
    return "$enteros.${decimales.toString().padStart(2, '0')}"
}