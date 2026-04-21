package com.moa.salary.app.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class HomeResponse(
    val workplace: String?,
    val workedEarnings: Long,
    val standardSalary: Long,
    val dailyPay: Long,
    val type: String,
    val events: List<String>,
    val clockInTime: String?,
    val clockOutTime: String?,
)