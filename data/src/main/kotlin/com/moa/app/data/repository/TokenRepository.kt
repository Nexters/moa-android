package com.moa.app.data.repository

interface TokenRepository {
    suspend fun postToken(token: String): String

    suspend fun saveAccessToken(token: String)

    suspend fun getAccessToken(): String?
    suspend fun clearToken()
}
