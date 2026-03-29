package com.moa.salary.app.data.repository

import com.moa.salary.app.core.model.work.Workday
import com.moa.salary.app.core.model.work.WorkdayType

interface WorkdayRepository {
    suspend fun getWorkday(date: String): Workday

    suspend fun updateWorkday(
        date: String,
        clockInTime: String?,
        clockOutTime: String?,
        type: WorkdayType,
    ): Workday

    suspend fun patchClockOUt(
        date: String,
        clockOutTime: String,
    ): Workday
}