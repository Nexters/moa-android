package com.moa.app.data.repository

import com.moa.app.core.model.history.Workday
import com.moa.app.core.model.history.WorkdayType
import com.moa.app.data.remote.api.WorkdayService
import com.moa.app.data.remote.model.request.UpdateWorkdayRequest
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

class WorkdayRepositoryImpl @Inject constructor(
    private val workdayService: WorkdayService,
) : WorkdayRepository {

    override suspend fun getWorkdays(year: Int, month: Int): ImmutableList<Workday> {
        return workdayService.getWorkdays(year, month).map { item ->
            Workday(
                date = item.date,
                type = WorkdayType.valueOf(item.type),
            )
        }.toImmutableList()
    }

    override suspend fun updateWorkday(
        date: String,
        type: WorkdayType,
        clockInTime: String?,
        clockOutTime: String?,
    ) {
        workdayService.putWorkday(
            date = date,
            request = UpdateWorkdayRequest(
                type = type.name,
                clockInTime = clockInTime,
                clockOutTime = clockOutTime,
            ),
        )
    }
}