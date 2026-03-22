package com.moa.salary.app.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class CalendarResponse(
    val earnings: EarningsResponse,
    val schedules: List<ScheduleResponse>,
    val joinedAt: String,
)

@Serializable
data class EarningsResponse(
    val workedMinutes: Long,
    val standardMinutes: Long,
    val workedEarnings: Long,
    val standardSalary: Long,
)

@Serializable
data class ScheduleResponse(
    val date: String,
    val type: String,
    val status: String,
    val events: List<String>,
    val dailyPay: Int,
    val clockInTime: String?,
    val clockOutTime: String?,
)