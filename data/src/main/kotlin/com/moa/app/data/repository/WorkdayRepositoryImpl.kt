package com.moa.app.data.repository

import com.moa.app.core.model.history.MonthlyWorkSummary
import com.moa.app.core.model.history.Workday
import com.moa.app.core.model.history.WorkdayDetail
import com.moa.app.core.model.history.WorkdayType
import com.moa.app.data.remote.api.WorkdayService
import com.moa.app.data.remote.model.request.ClockOutRequest
import com.moa.app.data.remote.model.request.WorkdayRequest
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
        val request = WorkdayRequest(
            type = type,
            clockInTime = clockInTime,
            clockOutTime = clockOutTime,
        )
        workdayService.updateWorkday(date, request)
    }

    override suspend fun updateClockOutTime(
        date: String,
        clockOutTime: String,
    ) {
        val request = ClockOutRequest(clockOutTime = clockOutTime)
        workdayService.patchClockOut(date, request)
    }

    override suspend fun getWorkdays(
        year: Int,
        month: Int,
    ): ImmutableList<Workday> {
        val response = workdayService.getWorkdays(year, month)
        return response.map { item ->
            Workday(
                date = item.date,
                type = parseWorkdayType(item.type),
            )
        }.toImmutableList()
    }

    override suspend fun getWorkdayDetail(
        date: String,
    ): WorkdayDetail {
        val response = workdayService.getWorkdayDetail(date)
        return WorkdayDetail(
            date = response.date,
            type = parseWorkdayType(response.type),
            clockInTime = response.clockInTime,
            clockOutTime = response.clockOutTime,
        )
    }

    override suspend fun getEarnings(
        year: Int,
        month: Int,
    ): MonthlyWorkSummary {
        val response = workdayService.getEarnings(year, month)
        return MonthlyWorkSummary(
            workedMinutes = response.workedMinutes,
            standardMinutes = response.standardMinutes,
            workedEarnings = response.workedEarnings,
            standardSalary = response.standardSalary,
        )
    }

    override suspend fun updateWorkday(
        date: String,
        type: WorkdayType,
        clockInTime: String?,
        clockOutTime: String?,
    ) {
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
        workdayService.updateWorkday(date, request)
    }

    private fun parseWorkdayType(type: String): WorkdayType {
        return when (type.uppercase()) {
            "WORK" -> WorkdayType.WORK
            "VACATION" -> WorkdayType.VACATION
            else -> WorkdayType.NONE
        }
    }
}