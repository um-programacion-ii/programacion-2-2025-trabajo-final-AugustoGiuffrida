package com.um.programacion2.network.model

import kotlinx.serialization.Serializable

@Serializable
data class EventoDTO(
    val id: Long,
    val titulo: String? = null,      // Puede ser null
    val resumen: String? = null,     // Puede ser null
    val descripcion: String? = null, // Puede ser null
    val fecha: String? = null,       // Puede ser null
    val precioEntrada: Double? = null,// Puede ser null (IMPORTANTE)
    val eventoTipo: EventoTipo? = null,
    val imagen: String? = null,      // Agregado por si acaso
    val filaAsientos: Int? = null,   // El backend log muestra que esto viene null también
    val columnAsientos: Int? = null,
    val direccion: String? = null,
)

@Serializable
data class EventoTipo(
    val id: Long? = null,
    val nombre: String? = null,
    val descripcion: String? = null
)