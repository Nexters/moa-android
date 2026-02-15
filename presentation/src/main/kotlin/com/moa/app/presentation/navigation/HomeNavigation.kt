package com.moa.app.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface HomeNavigation : NavKey {
    @Serializable
    data class BeforeWork(
        val todayEarnedSalary: Long? = null,
    ) : HomeNavigation

    @Serializable
    data class Working(
        val startHour: Int,
        val startMinute: Int,
        val endHour: Int,
        val endMinute: Int,
        val isOnVacation: Boolean = false,
        val isWorkDay: Boolean = true,
    ) : HomeNavigation

    @Serializable
    data class AfterWork(
        val todayEarnedSalary: Long,
        val startHour: Int,
        val startMinute: Int,
        val endHour: Int,
        val endMinute: Int,
        val isOnVacation: Boolean = false,
    ) : HomeNavigation

    data object Back : HomeNavigation
}