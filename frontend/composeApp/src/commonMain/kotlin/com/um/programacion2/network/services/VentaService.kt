package com.um.programacion2.network.services

import com.um.programacion2.network.NetworkUtils
import com.um.programacion2.network.model.VentaDTO
import io.ktor.client.call.body
import io.ktor.client.request.get

class VentaService {
    private val httpClient = NetworkUtils.httpClient

    suspend fun getUserVentas(): List<VentaDTO> {
        return try {
            httpClient.get("ventas").body()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}