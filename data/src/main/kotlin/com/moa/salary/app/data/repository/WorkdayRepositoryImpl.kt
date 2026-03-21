package com.moa.salary.app.data.repository

import com.moa.salary.app.core.model.work.MonthlyWorkSummary
import com.moa.salary.app.core.model.work.Workday
import com.moa.salary.app.core.model.work.WorkdayItem
import com.moa.salary.app.core.model.work.WorkdayType
import com.moa.salary.app.data.remote.api.WorkdayService
import com.moa.salary.app.data.remote.mapper.toDomain
import com.moa.salary.app.data.remote.model.request.ClockOutRequest
import com.moa.salary.app.data.remote.model.request.WorkdayRequest
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

class WorkdayRepositoryImpl @Inject constructor(
    private val workdayService: WorkdayService,
) : WorkdayRepository {

    override suspend fun updateWorkday(
        date: String,
        clockInTime: String,
        clockOutTime: String,
        type: WorkdayType,
    ): Workday {
        val request = WorkdayRequest(
            type = type.name,
            clockInTime = clockInTime,
            clockOutTime = clockOutTime,
        )
        return workdayService.updateWorkday(date, request).toDomain()
    }

    override suspend fun patchClockOUt(
        date: String,
        clockOutTime: String,
    ): Workday {
        val request = ClockOutRequest(clockOutTime = clockOutTime)
        return workdayService.patchClockOut(date, request).toDomain()
    }

    override suspend fun getWorkdays(
        year: Int,
        month: Int,
    ): ImmutableList<WorkdayItem> {
        val response = workdayService.getWorkdays(year, month)
        return response.map { it.toDomain() }.toImmutableList()
    }

    override suspend fun getWorkday(
        date: String,
    ): Workday {
        return workdayService.getWorkday(date).toDomain()
    }

    override suspend fun getEarnings(
        year: Int,
        month: Int,
    ): MonthlyWorkSummary {
        return workdayService.getEarnings(year, month).toDomain()
    }
}