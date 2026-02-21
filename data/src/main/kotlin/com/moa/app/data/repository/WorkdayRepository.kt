package com.moa.app.data.repository

import com.moa.app.core.model.history.Workday
import com.moa.app.core.model.history.WorkdayType
import kotlinx.collections.immutable.ImmutableList

interface WorkdayRepository {
    suspend fun getWorkdays(year: Int, month: Int): ImmutableList<Workday>

    suspend fun updateWorkday(
        date: String,
        type: WorkdayType,
        clockInTime: String?,
        clockOutTime: String?,
    )
}