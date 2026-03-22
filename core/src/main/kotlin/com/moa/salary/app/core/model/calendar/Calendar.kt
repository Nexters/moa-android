package com.moa.salary.app.core.model.calendar

import com.moa.salary.app.core.model.work.WorkdayType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import java.time.LocalDate

data class Calendar(
    val monthlyInfo: MonthlyInfo,
    val schedules: ImmutableMap<LocalDate, Schedule>,
    val joinedAt: LocalDate,
)

data class MonthlyInfo(
    val accumulatedWorkTime : String,
    val totalWorkTime : String,
    val accumulatedPay : String,
    val totalPay : String,
)

data class Schedule(
    val type: WorkdayType,
    val status: CalendarStatus,
    val events: ImmutableList<Event>,
    val dailyPay: Int,
    val workTime: String,
)