package com.moa.salary.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.moa.salary.app.data.security.TinkManager
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenDataStore @Inject constructor(
    private val context: Context,
    private val tinkManager: TinkManager,
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = DATASTORE_NAME
    )

    suspend fun saveTokens(accessToken: String, refreshToken: String?) {
        try {
            val encryptedAccessToken = tinkManager.encrypt(accessToken)
            val encryptedRefreshToken = refreshToken?.let { tinkManager.encrypt(it) }
            context.dataStore.edit { preferences ->
                preferences[ACCESS_TOKEN_KEY] = encryptedAccessToken
                if (encryptedRefreshToken != null) {
                    preferences[REFRESH_TOKEN_KEY] = encryptedRefreshToken
                } else {
                    preferences.remove(REFRESH_TOKEN_KEY)
                }
            }
        } catch (e: Exception) {
            throw Exception("Failed to save tokens", e)
        }
    }

    suspend fun getAccessToken(): String? = getDecrypted(ACCESS_TOKEN_KEY)

    suspend fun getRefreshToken(): String? = getDecrypted(REFRESH_TOKEN_KEY)

    suspend fun clearTokens() {
        try {
            context.dataStore.edit { preferences ->
                preferences.remove(ACCESS_TOKEN_KEY)
                preferences.remove(REFRESH_TOKEN_KEY)
            }
        } catch (e: Exception) {
            throw Exception("Failed to clear tokens", e)
        }
    }

    private suspend fun getDecrypted(key: Preferences.Key<String>): String? {
        return try {
            val preferences = context.dataStore.data.first()
            preferences[key]?.let { tinkManager.decrypt(it) }
        } catch (_: Exception) {
            null
        }
    }

    companion object {
        private const val DATASTORE_NAME = "moa_token_datastore"
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }
}
