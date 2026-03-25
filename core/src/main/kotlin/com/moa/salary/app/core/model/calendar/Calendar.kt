package com.moa.salary.app.core.model.calendar

import androidx.compose.runtime.Stable
import com.moa.salary.app.core.model.work.WorkdayType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Stable
data class Calendar(
    val monthlyInfo: MonthlyInfo,
    val schedules: ImmutableMap<LocalDate, Schedule>,
    val joinedAt: LocalDate,
)

@Stable
data class MonthlyInfo(
    val accumulatedWorkTime : String,
    val totalWorkTime : String,
    val accumulatedPay : String,
    val totalPay : String,
)

@Stable
@Serializable
data class Schedule(
    val type: WorkdayType,
    val status: CalendarStatus,
    val events: ImmutableList<Event>,
    val dailyPay: Int,
    val workTime: String,
)