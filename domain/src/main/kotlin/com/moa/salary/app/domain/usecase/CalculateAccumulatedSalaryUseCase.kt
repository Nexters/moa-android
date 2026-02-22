package com.moa.salary.app.domain.usecase

import java.time.LocalTime
import javax.inject.Inject

class CalculateAccumulatedSalaryUseCase @Inject constructor() {
    operator fun invoke(
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int,
        dailyPay: Long,
        currentTime: LocalTime = LocalTime.now(),
    ): Long {
        if (dailyPay <= 0) return 0L

        val startTimeSeconds = startHour * 3600 + startMinute * 60
        val endTimeSeconds = endHour * 3600 + endMinute * 60
        val currentTimeSeconds = currentTime.hour * 3600 + currentTime.minute * 60 + currentTime.second

        val totalWorkSeconds = endTimeSeconds - startTimeSeconds
        if (totalWorkSeconds <= 0) return 0L

        val workedSeconds = when {
            currentTimeSeconds < startTimeSeconds -> 0
            currentTimeSeconds > endTimeSeconds -> totalWorkSeconds
            else -> currentTimeSeconds - startTimeSeconds
        }

        val salaryPerSecond = dailyPay.toDouble() / totalWorkSeconds

        return (workedSeconds * salaryPerSecond).toLong()
    }

    fun calculateSalaryPerSecond(
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int,
        dailyPay: Long,
    ): Double {
        if (dailyPay <= 0) return 0.0

        val startTimeSeconds = startHour * 3600 + startMinute * 60
        val endTimeSeconds = endHour * 3600 + endMinute * 60
        val totalWorkSeconds = endTimeSeconds - startTimeSeconds

        return if (totalWorkSeconds > 0) {
            dailyPay.toDouble() / totalWorkSeconds
        } else {
            0.0
        }
    }

    fun calculateSalaryForWorkedTime(
        workedSeconds: Int,
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int,
        dailyPay: Long,
    ): Long {
        val salaryPerSecond = calculateSalaryPerSecond(
            startHour, startMinute, endHour, endMinute, dailyPay
        )
        return (workedSeconds * salaryPerSecond).toLong()
    }

    fun calculateMonthlyAccumulatedSalary(
        workedEarnings: Long,
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int,
        dailyPay: Long,
        currentTime: LocalTime = LocalTime.now(),
    ): Long {
        val todayEarnings = invoke(
            startHour = startHour,
            startMinute = startMinute,
            endHour = endHour,
            endMinute = endMinute,
            dailyPay = dailyPay,
            currentTime = currentTime,
        )
        return workedEarnings + todayEarnings
    }
}