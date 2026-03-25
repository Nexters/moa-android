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