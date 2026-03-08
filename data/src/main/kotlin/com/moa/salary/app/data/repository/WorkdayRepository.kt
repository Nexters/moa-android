package com.moa.salary.app.data.repository

import com.moa.salary.app.core.model.work.MonthlyWorkSummary
import com.moa.salary.app.core.model.work.Workday
import com.moa.salary.app.core.model.work.WorkdayItem
import kotlinx.collections.immutable.ImmutableList

interface WorkdayRepository {
    suspend fun updateWorkday(
        date: String,
        clockInTime: String,
        clockOutTime: String,
        type: String,
    ): Workday

    suspend fun patchClockOUt(
        date: String,
        clockOutTime: String,
    ): Workday

    suspend fun getWorkdays(
        year: Int,
        month: Int,
    ): ImmutableList<WorkdayItem>

    suspend fun getWorkday(
        date: String,
    ): Workday

    suspend fun getEarnings(
        year: Int,
        month: Int,
    ): MonthlyWorkSummary
}