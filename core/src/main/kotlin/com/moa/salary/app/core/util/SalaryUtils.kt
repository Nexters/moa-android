package com.moa.salary.app.core.util

import java.time.Duration
import java.time.LocalDateTime

object SalaryUtils {
    fun calculateSalaryForWorkedTime(
        clockInDateTime: LocalDateTime,
        clockOutDateTime: LocalDateTime,
        dailyPay: Long,
        workedSeconds: Int,
    ): Long {
        val salaryPerSecond = calculateSalaryPerSecond(
            clockInDateTime = clockInDateTime,
            clockOutDateTime = clockOutDateTime,
            dailyPay = dailyPay
        )

        return (workedSeconds * salaryPerSecond).toLong()
    }

    fun calculateSalaryForWorkedTime(
        clockInDateTime: LocalDateTime,
        clockOutDateTime: LocalDateTime,
        dailyPay: Long,
    ): Long {
        val now = LocalDateTime.now()
        val effectiveEnd = if (now.isBefore(clockOutDateTime)) now else clockOutDateTime
        val workedSeconds = Duration.between(clockInDateTime, effectiveEnd)
            .seconds
            .coerceAtLeast(0L)
            .toInt()

        val salaryPerSecond = calculateSalaryPerSecond(
            clockInDateTime, clockOutDateTime, dailyPay
        )

        return (workedSeconds * salaryPerSecond).toLong()
    }

    private fun calculateSalaryPerSecond(
        clockInDateTime: LocalDateTime,
        clockOutDateTime: LocalDateTime,
        dailyPay: Long,
    ): Double {
        if (dailyPay <= 0) return 0.0

        val totalWorkSeconds = Duration.between(clockInDateTime, clockOutDateTime).seconds

        return if (totalWorkSeconds > 0) {
            dailyPay.toDouble() / totalWorkSeconds
        } else {
            0.0
        }
    }
}