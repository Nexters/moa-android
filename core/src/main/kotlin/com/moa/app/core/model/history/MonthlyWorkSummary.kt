package com.moa.app.core.model.history

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Serializable
@Stable
data class MonthlyWorkSummary(
    val currentWorkHours: Int,
    val totalWorkHours: Int,
    val currentSalary: Long,
    val totalSalary: Long,
)