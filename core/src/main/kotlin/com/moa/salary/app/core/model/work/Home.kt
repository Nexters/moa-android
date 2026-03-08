package com.moa.salary.app.core.model.work

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Serializable
@Stable
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
