package com.moa.app.presentation.model

import kotlinx.serialization.Serializable

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
    ) : HomeNavigation {
        /**
         * 초당 급여를 계산합니다.
         * 하루 근무시간(초) = (퇴근시간 - 출근시간)
         * 초당 급여 = 일급 / 하루 근무시간(초)
         */
        fun calculateSalaryPerSecond(): Double {
            val startTotalSeconds = startHour * 3600 + startMinute * 60
            val endTotalSeconds = endHour * 3600 + endMinute * 60
            val totalWorkSeconds = endTotalSeconds - startTotalSeconds

            return if (totalWorkSeconds > 0 && dailyPay > 0) {
                dailyPay.toDouble() / totalWorkSeconds
            } else {
                0.0
            }
        }
    }

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