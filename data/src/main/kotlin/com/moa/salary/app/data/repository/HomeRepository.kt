package com.moa.salary.app.data.repository

import com.moa.salary.app.data.remote.model.response.HomeResponse

interface HomeRepository {
    suspend fun getHome(): HomeResponse
    suspend fun saveActualClockOut(clockOutTime: String)
    suspend fun clearActualClockOut()
    suspend fun saveAdjustedWorkTime(clockInTime: String, clockOutTime: String)
    suspend fun clearAdjustedWorkTime()
    suspend fun getShownNotificationBottomSheet(): Boolean
    suspend fun putShownNotificationBottomSheet(value: Boolean)
}