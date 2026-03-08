package com.moa.salary.app.core.model.work

import kotlinx.serialization.Serializable

@Serializable
data class Workday(
    val date: String,
    val type: WorkdayType,
    val dailyPay: Long,
    val startHour: Int?,
    val startMinute: Int?,
    val endHour : Int?,
    val endMinute : Int?,
)