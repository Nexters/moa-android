package com.moa.salary.app.data.repository

import com.moa.salary.app.core.model.work.Calendar
import com.moa.salary.app.data.remote.api.CalendarService
import com.moa.salary.app.data.remote.mapper.toDomain
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val calendarService: CalendarService,
) : CalendarRepository {

    override suspend fun getCalendar(
        year: Int,
        month: Int
    ): Calendar {
        return calendarService.getCalendar(
            year = year,
            month = month,
        ).toDomain()
    }
}