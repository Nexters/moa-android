package com.moa.salary.app.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class WorkdayResponse(
    val date: String,
    val type: String,
    val dailyPay: Long,
    val clockInTime: String?,
    val clockOutTime: String?,
)

@Serializable
data class WorkdayItemResponse(
    val date: String,
    val type: String,
)

@Serializable
data class EarningsResponse(
    val workedMinutes: Int,
    val standardMinutes: Int,
    val workedEarnings: Long,
    val standardSalary: Long,
)