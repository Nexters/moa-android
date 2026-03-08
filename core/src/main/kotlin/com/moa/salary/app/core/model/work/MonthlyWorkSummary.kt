package com.moa.salary.app.core.model.work

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Serializable
@Stable
data class MonthlyWorkSummary(
    val workedMinutes: Int,
    val standardMinutes: Int,
    val workedEarnings: Long,
    val standardSalary: Long,
)