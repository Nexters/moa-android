package com.moa.salary.app.data.repository

interface TokenRepository {
    suspend fun saveAccessToken(token: String)

    suspend fun getAccessToken(): String?
    suspend fun clearToken()
}
