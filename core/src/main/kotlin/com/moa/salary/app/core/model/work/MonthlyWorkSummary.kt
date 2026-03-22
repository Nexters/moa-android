package com.moa.salary.app.core.model.work

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Serializable
@Stable
data class MonthlyWorkSummary(
    val workedMinutes: Long,
    val standardMinutes: Long,
    val workedEarnings: Long,
    val standardSalary: Long,
)