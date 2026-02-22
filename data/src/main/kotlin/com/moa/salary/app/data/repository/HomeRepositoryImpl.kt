package com.moa.salary.app.data.repository

import com.moa.salary.app.data.local.PreferencesDataStore
import com.moa.salary.app.data.remote.api.MoaService
import com.moa.salary.app.data.remote.model.response.HomeResponse
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val moaService: MoaService,
    private val preferencesDataStore: PreferencesDataStore,
) : HomeRepository {
    override suspend fun getHome(): HomeResponse {
        val response = moaService.getHome()
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

        val adjustedClockIn = preferencesDataStore.getAdjustedClockIn(today)
        val adjustedClockOut = preferencesDataStore.getAdjustedClockOut(today)
        val actualClockOut = preferencesDataStore.getActualClockOut(today)

        return response.copy(
            clockInTime = adjustedClockIn ?: response.clockInTime,
            clockOutTime = actualClockOut ?: adjustedClockOut ?: response.clockOutTime,
        )
    }

    override suspend fun saveActualClockOut(clockOutTime: String) {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        preferencesDataStore.saveActualClockOut(today, clockOutTime)
    }

    override suspend fun clearActualClockOut() {
        preferencesDataStore.clearActualClockOut()
    }

    override suspend fun saveAdjustedWorkTime(clockInTime: String, clockOutTime: String) {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        preferencesDataStore.saveAdjustedWorkTime(today, clockInTime, clockOutTime)
    }

    override suspend fun clearAdjustedWorkTime() {
        preferencesDataStore.clearAdjustedWorkTime()
    }
}