package com.moa.salary.app.core.model.work

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableMap
import java.time.LocalDate

@Stable
data class Calendar(
    val monthlyInfo: MonthlyInfo,
    val workdays: ImmutableMap<LocalDate, Workday>,
    val joinedAt: LocalDate,
)

@Stable
data class MonthlyInfo(
    val accumulatedWorkTime: String,
    val totalWorkTime: String,
    val accumulatedPay: String,
    val totalPay: String,
)