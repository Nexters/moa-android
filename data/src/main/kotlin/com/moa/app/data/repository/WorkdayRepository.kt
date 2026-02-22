package com.moa.app.data.repository

import com.moa.app.core.model.history.MonthlyWorkSummary
import com.moa.app.core.model.history.Workday
import com.moa.app.core.model.history.WorkdayDetail
import com.moa.app.core.model.history.WorkdayType
import kotlinx.collections.immutable.ImmutableList

interface WorkdayRepository {
    suspend fun updateWorkTime(
        date: String,
        clockInTime: String,
        clockOutTime: String,
        type: String = "WORK",
    )

    suspend fun updateClockOutTime(
        date: String,
        clockOutTime: String,
    )

    suspend fun getWorkdays(
        year: Int,
        month: Int,
    ): ImmutableList<Workday>

    suspend fun getWorkdayDetail(
        date: String,
    ): WorkdayDetail

    suspend fun getEarnings(
        year: Int,
        month: Int,
    ): MonthlyWorkSummary

    suspend fun updateWorkday(
        date: String,
        type: WorkdayType,
        clockInTime: String?,
        clockOutTime: String?,
    )
}