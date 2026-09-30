package com.moa.salary.app.data.repository

import kotlinx.coroutines.flow.SharedFlow

interface TokenRepository {
    val sessionExpired: SharedFlow<Unit>

    suspend fun saveTokens(accessToken: String, refreshToken: String?)

    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun clearToken()
    suspend fun expireSession()
}
