package com.moa.app.data.repository

import android.util.Log
import com.moa.app.core.model.history.MonthlyWorkSummary
import com.moa.app.core.model.history.Workday
import com.moa.app.core.model.history.WorkdayDetail
import com.moa.app.core.model.history.WorkdayType
import com.moa.app.data.remote.api.WorkdayService
import com.moa.app.data.remote.dto.request.ClockOutRequest
import com.moa.app.data.remote.dto.request.WorkdayRequest
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

class WorkdayRepositoryImpl @Inject constructor(
    private val workdayService: WorkdayService,
) : WorkdayRepository {

    override suspend fun updateWorkTime(
        date: String,
        clockInTime: String,
        clockOutTime: String,
        type: String,
    ) {
        Log.d(TAG, "[Repository] updateWorkTime called - date: $date, type: $type, clockIn: $clockInTime, clockOut: $clockOutTime")

        val request = WorkdayRequest(
            type = type,
            clockInTime = clockInTime,
            clockOutTime = clockOutTime,
        )

        Log.d(TAG, "[Repository] Request body: $request")

        try {
            val content = workdayService.updateWorkday(date, request)
            Log.d(TAG, "[Repository] updateWorkTime success: $content")
        } catch (e: Exception) {
            Log.e(TAG, "[Repository] updateWorkTime failed", e)
            throw e
        }
    }

    override suspend fun updateClockOutTime(
        date: String,
        clockOutTime: String,
    ) {
        Log.d(TAG, "[Repository] updateClockOutTime called - date: $date, clockOut: $clockOutTime")

        val request = ClockOutRequest(clockOutTime = clockOutTime)

        Log.d(TAG, "[Repository] Request body: $request")

        try {
            val content = workdayService.patchClockOut(date, request)
            Log.d(TAG, "[Repository] updateClockOutTime success: $content")
        } catch (e: Exception) {
            Log.e(TAG, "[Repository] updateClockOutTime failed", e)
            throw e
        }
    }

    override suspend fun getWorkdays(
        year: Int,
        month: Int,
    ): ImmutableList<Workday> {
        Log.d(TAG, "[Repository] getWorkdays called - year: $year, month: $month")

        return try {
            val response = workdayService.getWorkdays(year, month)
            Log.d(TAG, "[Repository] getWorkdays success: ${response.size} items")
            response.map { item ->
                Workday(
                    date = item.date,
                    type = parseWorkdayType(item.type),
                )
            }.toImmutableList()
        } catch (e: Exception) {
            Log.e(TAG, "[Repository] getWorkdays failed", e)
            throw e
        }
    }

    override suspend fun getWorkdayDetail(
        date: String,
    ): WorkdayDetail {
        Log.d(TAG, "[Repository] getWorkdayDetail called - date: $date")

        return try {
            val response = workdayService.getWorkdayDetail(date)
            Log.d(TAG, "[Repository] getWorkdayDetail success: $response")
            WorkdayDetail(
                date = response.date,
                type = parseWorkdayType(response.type),
                clockInTime = response.clockInTime,
                clockOutTime = response.clockOutTime,
            )
        } catch (e: Exception) {
            Log.e(TAG, "[Repository] getWorkdayDetail failed", e)
            throw e
        }
    }

    override suspend fun getEarnings(
        year: Int,
        month: Int,
    ): MonthlyWorkSummary {
        Log.d(TAG, "[Repository] getEarnings called - year: $year, month: $month")

        return try {
            val response = workdayService.getEarnings(year, month)
            Log.d(TAG, "[Repository] getEarnings success: $response")
            MonthlyWorkSummary(
                workedMinutes = response.workedMinutes,
                standardMinutes = response.standardMinutes,
                workedEarnings = response.workedEarnings,
                standardSalary = response.standardSalary,
            )
        } catch (e: Exception) {
            Log.e(TAG, "[Repository] getEarnings failed", e)
            throw e
        }
    }

    override suspend fun updateWorkday(
        date: String,
        type: WorkdayType,
        clockInTime: String?,
        clockOutTime: String?,
    ) {
        Log.d(TAG, "[Repository] updateWorkday called - date: $date, type: $type, clockIn: $clockInTime, clockOut: $clockOutTime")

        val typeString = when (type) {
            WorkdayType.WORK -> "WORK"
            WorkdayType.VACATION -> "VACATION"
            WorkdayType.NONE -> "NONE"
        }

        val request = WorkdayRequest(
            type = typeString,
            clockInTime = clockInTime ?: "09:00",
            clockOutTime = clockOutTime ?: "18:00",
        )

        Log.d(TAG, "[Repository] Request body: $request")

        try {
            val content = workdayService.updateWorkday(date, request)
            Log.d(TAG, "[Repository] updateWorkday success: $content")
        } catch (e: Exception) {
            Log.e(TAG, "[Repository] updateWorkday failed", e)
            throw e
        }
    }

    private fun parseWorkdayType(type: String): WorkdayType {
        return when (type.uppercase()) {
            "WORK" -> WorkdayType.WORK
            "VACATION" -> WorkdayType.VACATION
            else -> WorkdayType.NONE
        }
    }

    companion object {
        private const val TAG = "WorkdayApi"
    }
}