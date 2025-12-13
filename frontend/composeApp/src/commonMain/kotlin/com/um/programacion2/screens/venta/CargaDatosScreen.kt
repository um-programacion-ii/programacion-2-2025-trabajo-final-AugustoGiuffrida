package com.um.programacion2.screens.compra

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.um.programacion2.network.model.AccountDTO
import com.um.programacion2.network.model.AsientoDTO
import com.um.programacion2.network.model.ConfirmarCompraDTO
import com.um.programacion2.network.model.DetalleAsientoCompra
import com.um.programacion2.network.model.EventoDTO
import com.um.programacion2.network.services.AccountService
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

data class CargaDatosScreen(
    val evento: EventoDTO,
    val asientosSeleccionados: List<AsientoDTO>
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scope = rememberCoroutineScope()

        // Servicios
        val accountService = remember { AccountService() }

        // Estado
        val nombresState = remember { mutableStateMapOf<String, String>() }
        var isLoadingUser by remember { mutableStateOf(true) }
        var userAccount by remember { mutableStateOf<AccountDTO?>(null) }

        LaunchedEffect(Unit) {
            scope.launch {
                try {
                    val account = accountService.getAccount()
                    userAccount = account
                    isLoadingUser = false

                    if (account != null) {
                        // Construir nombre completo o usar login como fallback
                        val nombreCompleto = listOfNotNull(account.firstName, account.lastName)
                            .joinToString(" ")
                            .ifBlank { account.login }

                        // Asignar este nombre a TODOS los asientos por defecto
                        asientosSeleccionados.forEach { asiento ->
                            val key = "${asiento.fila}-${asiento.columna}"
                            nombresState[key] = nombreCompleto
                        }
                    }
                } catch (e: Exception) {
                    // Manejar error silenciosamente
                    isLoadingUser = false
                }
            }
        }

        // Validación: Botón habilitado solo si hay nombres
        val isFormValid = asientosSeleccionados.all { asiento ->
            !nombresState["${asiento.fila}-${asiento.columna}"].isNullOrBlank()
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Confirmar Asistentes") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                )
            },
            bottomBar = {
                Button(
                    onClick = {
                        // --- 2. Preparar DTO para el Backend ---
                        val detalles = asientosSeleccionados.map { asiento ->
                            DetalleAsientoCompra(
                                fila = asiento.fila,
                                columna = asiento.columna,
                                nombrePersona = nombresState["${asiento.fila}-${asiento.columna}"] ?: ""
                            )
                        }

                        val compraDTO = ConfirmarCompraDTO(detalles = detalles)

                        println("--- INICIO PROCESO DE COMPRA ---")
                        println("Payload a enviar: ${Json.encodeToString(compraDTO)}")

                        // TODO: IMPLEMENTAR LA LÓGICA SECUENCIAL:
                        // 1. ventaService.seleccionarSesion(asientos)
                        // 2. ventaService.bloquear(bloqueoDTO)
                        // 3. ventaService.comprar(compraDTO)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    enabled = isFormValid && !isLoadingUser
                ) {
                    if (isLoadingUser) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Continuar al Pago")
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
                // Cabecera del Evento
                Text(
                    text = evento.titulo ?: "Evento",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (isLoadingUser) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    Text("Cargando tus datos...", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                } else {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, null)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Entradas asignadas a: ${userAccount?.login ?: "Usuario"}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Detalle de Entradas:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(asientosSeleccionados) { asiento ->
                        AsistenteItem(
                            asiento = asiento,
                            nombreValue = nombresState["${asiento.fila}-${asiento.columna}"] ?: "",
                            onNombreChange = { nuevoNombre ->
                                nombresState["${asiento.fila}-${asiento.columna}"] = nuevoNombre
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AsistenteItem(
    asiento: AsientoDTO,
    nombreValue: String,
    onNombreChange: (String) -> Unit
) {
    OutlinedTextField(
        value = nombreValue,
        onValueChange = onNombreChange,
        label = { Text("Fila ${asiento.fila} - Asiento ${asiento.columna}") },
        placeholder = { Text("Nombre del asistente") },
        leadingIcon = {
            Icon(Icons.Default.EventSeat, contentDescription = null)
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}