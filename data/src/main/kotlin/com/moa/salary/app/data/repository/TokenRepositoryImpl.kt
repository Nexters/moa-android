package com.moa.salary.app.data.repository

import com.moa.salary.app.data.local.TokenDataStore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val tokenDataStore: TokenDataStore,
) : TokenRepository {

    private val _sessionExpired = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    override val sessionExpired = _sessionExpired.asSharedFlow()

    override suspend fun saveTokens(accessToken: String, refreshToken: String?) {
        tokenDataStore.saveTokens(accessToken, refreshToken)
    }

    override suspend fun getAccessToken(): String? {
        return tokenDataStore.getAccessToken()
    }

    override suspend fun getRefreshToken(): String? {
        return tokenDataStore.getRefreshToken()
    }

    override suspend fun clearToken() {
        tokenDataStore.clearTokens()
    }

    override suspend fun expireSession() {
        tokenDataStore.clearTokens()
        _sessionExpired.emit(Unit)
    }
}
