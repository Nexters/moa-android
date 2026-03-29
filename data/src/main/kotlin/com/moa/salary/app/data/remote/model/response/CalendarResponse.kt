package com.moa.salary.app.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class CalendarResponse(
    val earnings: EarningsResponse,
    val schedules: List<WorkdayResponse>,
    val joinedAt: String,
)

@Serializable
data class EarningsResponse(
    val workedMinutes: Long,
    val standardMinutes: Long,
    val workedEarnings: Long,
    val standardSalary: Long,
)