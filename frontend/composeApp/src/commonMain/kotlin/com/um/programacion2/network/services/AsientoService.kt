package com.um.programacion2.network.services

import com.um.programacion2.network.NetworkUtils
import com.um.programacion2.network.model.AsientoRedisDTO
import com.um.programacion2.network.model.EventoRedisResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode

class AsientoService {
    private val httpClient = NetworkUtils.httpClient

    suspend fun getAsientosOcupados(eventoId: Long): List<AsientoRedisDTO> {
        return try {
            val response = httpClient.get("asientos-ocupados/eventos/$eventoId")

            if (response.status == HttpStatusCode.Companion.NoContent) {
                return emptyList()
            }

            val body: EventoRedisResponse = response.body()
            body.asientos
        } catch (e: Exception) {
            // Si hay error (o 204 sin body), asumo que no hay ocupados para no bloquear la UI
            println("Info: No se pudieron cargar asientos ocupados o está vacío. ${e.message}")
            emptyList()
        }
    }
}