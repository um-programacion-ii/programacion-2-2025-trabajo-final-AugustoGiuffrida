package com.um.programacion2.network.model


import kotlinx.serialization.Serializable

@Serializable
data class AsientoDTO(
    val fila: Int,
    val columna: Int,
    val estado: EstadoAsientoUI // "Libre", "Ocupado", "Bloqueado", "Vendido"
)

@Serializable
data class AsientosResponse(
    val eventoId: Long,
    val asientos: List<AsientoDTO>
)

enum class EstadoAsientoUI {
    LIBRE,
    OCUPADO,    // Incluye Vendido y Bloqueado por otros
    SELECCIONADO // Estado local del usuario actual
}

@Serializable
data class EventoRedisResponse(
    val eventoId: Long,
    val asientos: List<AsientoRedisDTO> = emptyList()
)

@Serializable
data class AsientoRedisDTO(
    val fila: Int,
    val columna: Int,
    val estado: String? = null,
    val expira: String? = null
)