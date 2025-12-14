package com.um.programacion2.network.model

import kotlinx.serialization.Serializable

@Serializable
data class AccountDTO(
    val id: Long? = null,
    val login: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val imageUrl: String? = null,
    val authorities: List<String> = emptyList() //["ROLE_USER", "ROLE_ADMIN"]
) {
    fun fullName(): String {
        return if (!firstName.isNullOrBlank() || !lastName.isNullOrBlank()) {
            "${firstName.orEmpty()} ${lastName.orEmpty()}".trim()
        } else {
            login
        }
    }
}