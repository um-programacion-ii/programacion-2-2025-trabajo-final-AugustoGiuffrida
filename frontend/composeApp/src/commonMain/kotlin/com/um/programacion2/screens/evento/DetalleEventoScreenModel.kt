package com.um.programacion2.screens.evento

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.um.programacion2.network.services.AsientoService
import com.um.programacion2.network.model.AsientoDTO
import com.um.programacion2.network.model.EstadoAsientoUI
import com.um.programacion2.network.model.EventoDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetalleState(
    val isLoading: Boolean = true,
    val asientos: List<AsientoDTO> = emptyList(), // grilla completa
    val seleccionados: Set<AsientoDTO> = emptySet(),
    val error: String? = null
)

class DetalleEventoScreenModel(
    private val asientoService: AsientoService,
    private val evento: EventoDTO
) : ScreenModel {

    private val _state = MutableStateFlow(DetalleState())
    val state = _state.asStateFlow()

    init {
        inicializarYConsultar()
    }

    private fun inicializarYConsultar() {
        screenModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            // 1. Obtener la "Lista Negra" (Asientos ocupados en Redis)
            val ocupadosRedis = try {
                asientoService.getAsientosOcupados(evento.id)
            } catch (e: Exception) {
                // Si falla, no hay ocupados para no bloquear la venta
                emptyList()
            }

            val ocupadosSet = ocupadosRedis.map { "${it.fila}-${it.columna}" }.toSet()

            // 2. Construir la Sala Completa
            val filas = evento.filaAsientos
            val columnas = evento.columnAsientos

            val matrizCompleta = mutableListOf<AsientoDTO>()

            for (f in 1..filas) {
                for (c in 1..columnas) {
                    val key = "$f-$c"
                    // Verificar si la coordenada está en el Set de ocupados
                    val estado = if (ocupadosSet.contains(key)) {
                        EstadoAsientoUI.OCUPADO
                    } else {
                        EstadoAsientoUI.LIBRE
                    }

                    matrizCompleta.add(AsientoDTO(f, c, estado))
                }
            }

            // 3. Actualizar la UI con la matriz lista
            _state.update {
                it.copy(
                    asientos = matrizCompleta,
                    isLoading = false
                )
            }
        }
    }

    fun toggleAsiento(asiento: AsientoDTO) {
        if (asiento.estado == EstadoAsientoUI.OCUPADO) return

        val actuales = _state.value.seleccionados
        if (actuales.contains(asiento)) {
            _state.update { it.copy(seleccionados = actuales - asiento) }
        } else {
            if (actuales.size < 4) {
                _state.update { it.copy(seleccionados = actuales + asiento) }
            }
        }
    }
}