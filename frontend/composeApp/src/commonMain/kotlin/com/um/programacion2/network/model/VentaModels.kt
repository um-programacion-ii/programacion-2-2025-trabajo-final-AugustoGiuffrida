package com.um.programacion2.network.model

import kotlinx.serialization.Serializable

@Serializable
enum class EstadoVenta {
    PENDIENTE,
    CONFIRMADA,
    RECHAZADA,
}

@Serializable
data class UserDTO(
    val id: Long,
    val login: String
)

@Serializable
data class VentaDTO(
    val id: Long,
    val ventaIdCatedra: Long? = null,
    val fechaVenta: String,
    val precioVenta: Double,
    val resultado: Boolean,
    val descripcion: String,
    val estadoVenta: EstadoVenta,
    val evento: EventoDTO,
    val user: UserDTO,
)