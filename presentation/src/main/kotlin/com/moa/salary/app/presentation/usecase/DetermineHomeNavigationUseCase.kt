package com.moa.salary.app.presentation.usecase

import com.moa.salary.app.core.extensions.toHourMinuteOrNull
import com.moa.salary.app.data.remote.model.response.HomeResponse
import com.moa.salary.app.data.remote.model.response.HomeType
import com.moa.salary.app.presentation.model.HomeNavigation
import java.time.LocalTime
import javax.inject.Inject

class DetermineHomeNavigationUseCase @Inject constructor() {
    operator fun invoke(
        response: HomeResponse,
        currentTime: LocalTime = LocalTime.now(),
    ): HomeNavigation {
        val clockIn = response.clockInTime?.toHourMinuteOrNull()
        val clockOut = response.clockOutTime?.toHourMinuteOrNull()

        val startHour = clockIn?.first ?: 9
        val startMinute = clockIn?.second ?: 0
        val endHour = clockOut?.first ?: 18
        val endMinute = clockOut?.second ?: 0

        val isWorkDay = response.type != HomeType.NONE
        val isOnVacation = response.type == HomeType.VACATION

        val clockInLocalTime = LocalTime.of(startHour, startMinute)
        val clockOutLocalTime = LocalTime.of(endHour, endMinute)

        val isOvernightShift = clockOutLocalTime < clockInLocalTime

        val isBeforeWork: Boolean
        val isAfterWork: Boolean

        if (isOvernightShift) {
            isBeforeWork = currentTime >= clockOutLocalTime && currentTime < clockInLocalTime
            isAfterWork = false
        } else {
            isBeforeWork = currentTime < clockInLocalTime
            isAfterWork = currentTime >= clockOutLocalTime
        }

        return when {
            isBeforeWork -> HomeNavigation.BeforeWork()
            isAfterWork -> HomeNavigation.Working(
                startHour = startHour,
                startMinute = startMinute,
                endHour = endHour,
                endMinute = endMinute,
                dailyPay = response.dailyPay,
                isOnVacation = isOnVacation,
                isWorkDay = isWorkDay,
                showWorkCompletionOverlay = true,
            )
            else -> HomeNavigation.Working(
                startHour = startHour,
                startMinute = startMinute,
                endHour = endHour,
                endMinute = endMinute,
                dailyPay = response.dailyPay,
                isOnVacation = isOnVacation,
                isWorkDay = isWorkDay,
            )
        }
    }
}