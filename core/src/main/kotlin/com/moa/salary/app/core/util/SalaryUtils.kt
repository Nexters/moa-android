package com.moa.salary.app.core.util

import java.time.LocalTime

object SalaryUtils {
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

    fun calculateSalaryForWorkedTime(
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int,
        dailyPay: Long,
    ): Long {
        val now = LocalTime.now()
        val endTime = LocalTime.of(endHour, endMinute)
        val totalSeconds = if (now.isBefore(endTime)) {
            now.hour * 3600 + now.minute * 60 + now.second
        } else {
            endHour * 3600 + endMinute * 60
        }
        val startTimeSeconds = startHour * 3600 + startMinute * 60
        val workedSeconds = totalSeconds - startTimeSeconds

        val salaryPerSecond = calculateSalaryPerSecond(
            startHour, startMinute, endHour, endMinute, dailyPay
        )

        return (workedSeconds * salaryPerSecond).toLong()
    }

    private fun calculateSalaryPerSecond(
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
}