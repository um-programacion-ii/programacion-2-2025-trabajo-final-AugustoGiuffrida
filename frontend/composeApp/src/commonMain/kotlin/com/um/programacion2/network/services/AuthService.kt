package com.um.programacion2.network.services

import com.russhwolf.settings.Settings
import com.um.programacion2.network.NetworkUtils
import com.um.programacion2.network.model.JWTToken
import com.um.programacion2.network.model.LoginRequest
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthService {

    private val httpClient = NetworkUtils.httpClient
    private val settings = Settings()

    suspend fun authenticate(request: LoginRequest): Boolean {
        return try {
            val response: JWTToken = httpClient.post("authenticate") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()

            // guardar token en almacenamiento local
            settings.putString("jwt_token", response.idToken)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun logout() {
        settings.remove("jwt_token")
    }

    fun isLoggedIn(): Boolean {
        return settings.getStringOrNull("jwt_token") != null
    }
}