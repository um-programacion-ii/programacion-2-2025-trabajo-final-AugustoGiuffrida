package com.um.programacion2.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val username: String,
    val password: String,
    val rememberMe: Boolean = false
)

@Serializable
data class JWTToken(
    @SerialName("id_token") val idToken: String
)

// manejar errores de login
@Serializable
data class ErrorResponse(
    val title: String? = null,
    val status: Int? = null,
    val detail: String? = null
)