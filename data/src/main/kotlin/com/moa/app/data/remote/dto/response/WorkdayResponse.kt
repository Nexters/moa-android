package com.moa.app.data.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class WorkdayResponse(
    val code: String,
    val message: String,
    val content: WorkdayContent,
)

@Serializable
data class WorkdayContent(
    val date: String,
    val type: String,
    val dailyPay: Long,
    val clockInTime: String,
    val clockOutTime: String,
)

@Serializable
data class WorkdayListItem(
    val date: String,
    val type: String,
)

@Serializable
data class WorkdayDetailResponse(
    val date: String,
    val type: String,
    val clockInTime: String?,
    val clockOutTime: String?,
)

@Serializable
data class EarningsResponse(
    val workedMinutes: Int,
    val standardMinutes: Int,
    val workedEarnings: Long,
    val standardSalary: Long,
)