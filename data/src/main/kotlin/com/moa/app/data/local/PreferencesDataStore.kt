package com.moa.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
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

    suspend fun setNotificationPermissionRequested() {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATION_PERMISSION_REQUESTED_KEY] = true
        }
    }

    suspend fun isNotificationPermissionRequested(): Boolean {
        val preferences = context.dataStore.data.first()
        return preferences[NOTIFICATION_PERMISSION_REQUESTED_KEY] ?: false
    }

    suspend fun setPaydayDay(day: Int) {
        context.dataStore.edit { preferences ->
            preferences[PAYDAY_DAY_KEY] = day
        }
    }

    suspend fun getPaydayDay(): Int? {
        val preferences = context.dataStore.data.first()
        return preferences[PAYDAY_DAY_KEY]
    }

    companion object {
        private const val DATASTORE_NAME = "moa_preferences_datastore"
        private val NOTIFICATION_PERMISSION_REQUESTED_KEY =
            booleanPreferencesKey("notification_permission_requested")
        private val PAYDAY_DAY_KEY =
            intPreferencesKey("payday_day")
    }
}