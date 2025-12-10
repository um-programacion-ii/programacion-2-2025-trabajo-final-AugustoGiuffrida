package com.um.programacion2.network.services

import com.um.programacion2.network.NetworkUtils
import com.um.programacion2.network.model.AccountDTO
import io.ktor.client.call.body
import io.ktor.client.request.get

class AccountService {
    private val httpClient = NetworkUtils.httpClient

    suspend fun getAccount(): AccountDTO? {
        return try {
            httpClient.get("account").body()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}