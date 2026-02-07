package com.moa.app.data.repository

import com.moa.app.data.local.TokenDataStore
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val tokenDataStore: TokenDataStore,
) : TokenRepository {

    override suspend fun postToken(token: String): String {
        // TODO post token
        return "token"
    }

    override suspend fun saveAccessToken(token: String) {
        tokenDataStore.saveAccessToken(token)
    }

    override suspend fun getAccessToken(): String? {
        return tokenDataStore.getAccessToken()
    }

    override suspend fun clearToken() {
        tokenDataStore.clearAccessToken()
    }
}
