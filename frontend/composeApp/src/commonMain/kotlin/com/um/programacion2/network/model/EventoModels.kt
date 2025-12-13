package com.um.programacion2.network.model

import kotlinx.serialization.Serializable

@Serializable
data class EventoDTO(
    val id: Long,
    val titulo: String? = null,
    val resumen: String? = null,
    val descripcion: String? = null,
    val fecha: String? = null,
    val precioEntrada: Double? = null,
    val eventoTipo: EventoTipo? = null,
    val imagen: String? = null,
    val filaAsientos: Int? = null,
    val columnAsientos: Int? = null,
    val direccion: String? = null,
)

@Serializable
data class EventoTipo(
    val id: Long? = null,
    val nombre: String? = null,
    val descripcion: String? = null
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