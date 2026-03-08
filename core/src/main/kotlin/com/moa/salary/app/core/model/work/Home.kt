package com.moa.salary.app.core.model.work

import kotlinx.serialization.Serializable

@Serializable
data class Home(
    val workplace: String?,
    val workedEarnings: Long,
    val standardSalary: Long,
    val dailyPay: Long,
    val type: WorkdayType,
    val startHour: Int,
    val startMinute: Int,
    val endHour: Int,
    val endMinute: Int,
)
