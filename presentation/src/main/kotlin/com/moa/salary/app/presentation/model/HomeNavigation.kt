package com.moa.salary.app.presentation.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface HomeNavigation : RootNavigation {
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
        val dailyPay: Long = 0L,
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