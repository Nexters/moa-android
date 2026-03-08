package com.moa.salary.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = DATASTORE_NAME
    )

    suspend fun getBoolean(key: String): Boolean? {
        return try {
            val preferences = context.dataStore.data.first()
            preferences[booleanPreferencesKey(key)]
        } catch (_: Exception) {
            null
        }
    }

    suspend fun putBoolean(key: String, value: Boolean) {
        try {
            context.dataStore.edit { preferences ->
                preferences[booleanPreferencesKey(key)] = value
            }
        } catch (e: Exception) {
            throw Exception("Failed to save preference", e)
        }
    }

    suspend fun getString(key: String): String? {
        return try {
            val preferences = context.dataStore.data.first()
            preferences[stringPreferencesKey(key)]
        } catch (_: Exception) {
            null
        }
    }

    suspend fun putString(key: String, value: String) {
        try {
            context.dataStore.edit { preferences ->
                preferences[stringPreferencesKey(key)] = value
            }
        } catch (e: Exception) {
            throw Exception("Failed to save preference", e)
        }
    }

    suspend fun remove(key: String) {
        try {
            context.dataStore.edit { preferences ->
                preferences.remove(stringPreferencesKey(key))
            }
        } catch (e: Exception) {
            throw Exception("Failed to remove preference", e)
        }
    }

    suspend fun clear() {
        try {
            context.dataStore.edit { preferences ->
                preferences.clear()
            }
        } catch (e: Exception) {
            throw Exception("Failed to clear preferences", e)
        }
    }

    suspend fun getShownNotificationBottomSheet(): Boolean {
        return getBoolean(KEY_SHOWN_NOTIFICATION_BOTTOM_SHEET) ?: false
    }

    suspend fun putShownNotificationBottomSheet(value: Boolean) {
        putBoolean(KEY_SHOWN_NOTIFICATION_BOTTOM_SHEET, value)
    }

    companion object {
        private const val DATASTORE_NAME = "moa_preferences_datastore"
        private const val KEY_SHOWN_NOTIFICATION_BOTTOM_SHEET = "shown_notification_bottom_sheet"
    }
}