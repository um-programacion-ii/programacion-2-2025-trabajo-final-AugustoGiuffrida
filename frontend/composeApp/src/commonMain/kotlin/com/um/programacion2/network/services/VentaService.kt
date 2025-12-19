package com.um.programacion2.network.services

import com.um.programacion2.network.NetworkUtils
import com.um.programacion2.network.model.VentaDTO
import io.ktor.client.call.body
import io.ktor.client.request.get
import com.um.programacion2.network.model.*
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
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

    suspend fun actualizarSesion(sesion: SesionVentaDTO): Boolean {
        return try {
            val response = httpClient.post("sesion") {
                contentType(ContentType.Application.Json)
                setBody(sesion)
            }
            response.status.isSuccess()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun bloquearAsientos(solicitud: SolicitudBloqueoDTO): Boolean {
        return try {
            val response = httpClient.post("ventas/bloquear") {
                contentType(ContentType.Application.Json)
                setBody(solicitud)
            }
            response.status.isSuccess()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun comprar(compra: ConfirmarCompraDTO): VentaDTO? {
        return try {
            httpClient.post("ventas/comprar") {
                contentType(ContentType.Application.Json)
                setBody(compra)
            }.body()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getSesionActual(): SesionVentaDTO? {
        return try {
            // GET al endpoint que consulta Redis
            httpClient.get("sesion").body()
        } catch (e: Exception) {
            null
        }
    }
}
