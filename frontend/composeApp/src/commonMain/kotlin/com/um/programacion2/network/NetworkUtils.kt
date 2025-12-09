package com.um.programacion2.network

import androidx.compose.ui.text.font.FontVariation
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import com.russhwolf.settings.Settings

object NetworkUtils {
    // instancia de Settings para guardar/leer el token
    private val settings: Settings = Settings()
    val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(
                json = Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                },
                contentType = ContentType.Any
            )
        }

        defaultRequest {

            url("http://10.0.2.2:8080/api/")

            // si hay token guardado, se inyecta en cada peticion
            val token = settings.getStringOrNull("jwt_token")
            if (!token.isNullOrBlank()) {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }
}