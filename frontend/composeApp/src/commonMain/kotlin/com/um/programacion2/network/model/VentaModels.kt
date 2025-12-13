package com.um.programacion2.network.model

import kotlinx.serialization.Serializable

@Serializable
enum class EstadoVenta {
    PENDIENTE,
    CONFIRMADA,
    RECHAZADA,
}


@Serializable
enum class EstadoSesion {
    SELECCIONANDO,
    CONFIRMANDO
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

@Serializable
data class ConfirmarCompraDTO(
    val detalles: List<DetalleAsientoCompra>,
    val medioPago: String = "Efectivo"
)

@Serializable
data class DetalleAsientoCompra(
    val fila: Int,
    val columna: Int,
    val nombrePersona: String
)


@Serializable
data class AsientoSesionDTO(
    val fila: Int,
    val columna: Int
)

@Serializable
data class SesionVentaDTO(
    val eventoId: Long,
    val nombreEvento: String?,
    val asientosSeleccionados: List<AsientoSesionDTO>,
    val estadoActual: EstadoSesion?
)

@Serializable
data class SolicitudBloqueoDTO(
    val eventoId: Long,
    val asientos: List<AsientoSesionDTO>
)