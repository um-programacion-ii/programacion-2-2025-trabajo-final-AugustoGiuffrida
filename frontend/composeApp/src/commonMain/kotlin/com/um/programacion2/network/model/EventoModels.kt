package com.um.programacion2.network.model

import kotlinx.serialization.Serializable

@Serializable
data class EventoDTO(
    val id: Long,
    val titulo: String,
    val resumen: String,
    val descripcion: String,
    val fecha: String,
    val precioEntrada: Double,
    val eventoTipo: EventoTipo? = null
)

@Serializable
data class EventoTipo(
    val id: Long? = null,
    val nombre: String,
    val descripcion: String? = null
)