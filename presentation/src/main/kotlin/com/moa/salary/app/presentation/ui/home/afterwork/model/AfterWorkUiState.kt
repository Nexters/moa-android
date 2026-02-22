package com.moa.salary.app.presentation.ui.home.afterwork.model

import androidx.compose.runtime.Stable
import com.moa.salary.app.core.extensions.formatCurrency
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Stable
data class AfterWorkUiState(
    val today: LocalDate = LocalDate.now(),
    val location: String? = null,
    val month: Int = LocalDate.now().monthValue,
    val todaySalary: Long = 0L,
    val workedEarnings: Long = 0L,
    val standardSalary: Long = 0L,
    val dailyPay: Long = 0L,
    val startHour: Int = 9,
    val startMinute: Int = 0,
    val endHour: Int = 18,
    val endMinute: Int = 0,
    val isOnVacation: Boolean = false,
    val showMoreWorkBottomSheet: Boolean = false,
    val showConfetti: Boolean = true,
) {
    val monthlyAccumulatedSalary: Long
        get() = workedEarnings + todaySalary

    val accumulatedSalary: String
        get() = formatCurrency(monthlyAccumulatedSalary)

    val dateDisplay: String
        get() = today.format(DateTimeFormatter.ofPattern("M월 d일 (E)", Locale.KOREAN))

    val todaySalaryWithPlusDisplay: String
        get() = "+${formatCurrency(todaySalary)}원"

    val startTimeDisplay: String
        get() = "${startHour.toString().padStart(2, '0')}:${
            startMinute.toString().padStart(2, '0')
        }"

    val endTimeDisplay: String
        get() = "${endHour.toString().padStart(2, '0')}:${endMinute.toString().padStart(2, '0')}"

    val workTimeDisplay: String
        get() = "$startTimeDisplay - $endTimeDisplay"
}