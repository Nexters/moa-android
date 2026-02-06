package com.moa.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.moa.app.data.security.TinkManager
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

    suspend fun saveAccessToken(token: String) {
        try {
            val encryptedToken = tinkManager.encrypt(token)
            context.dataStore.edit { preferences ->
                preferences[ACCESS_TOKEN_KEY] = encryptedToken
            }
        } catch (e: Exception) {
            throw Exception("Failed to save access token", e)
        }
    }

    suspend fun getAccessToken(): String? {
        return try {
            val preferences = context.dataStore.data.first()
            val encryptedToken = preferences[ACCESS_TOKEN_KEY]

            if (encryptedToken != null) {
                tinkManager.decrypt(encryptedToken)
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    suspend fun clearAccessToken() {
        try {
            context.dataStore.edit { preferences ->
                preferences.remove(ACCESS_TOKEN_KEY)
            }
        } catch (e: Exception) {
            throw Exception("Failed to clear access token", e)
        }
    }

    companion object {
        private const val DATASTORE_NAME = "moa_token_datastore"
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    }
}
