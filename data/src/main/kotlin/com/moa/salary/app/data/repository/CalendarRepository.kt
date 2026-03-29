package com.moa.salary.app.data.repository

import com.moa.salary.app.core.model.work.Calendar

interface CalendarRepository {
    suspend fun getCalendar(
        year: Int,
        month: Int,
    ): Calendar
}