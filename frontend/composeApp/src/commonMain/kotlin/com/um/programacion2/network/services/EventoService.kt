package com.um.programacion2.network.services

import com.um.programacion2.network.NetworkUtils
import com.um.programacion2.network.model.EventoDTO
import io.ktor.client.call.body
import io.ktor.client.request.get

class EventoService {

    private val httpClient = NetworkUtils.httpClient

    suspend fun getAllEventos(): List<EventoDTO> {
        return try {
            httpClient.get("eventos").body()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getEventoById(id: Long): EventoDTO? {
        return try {
            httpClient.get("eventos/$id").body()
        } catch (e: Exception) {
            null
        }
    }
}