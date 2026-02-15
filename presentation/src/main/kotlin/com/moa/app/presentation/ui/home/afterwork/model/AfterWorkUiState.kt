package com.moa.app.presentation.ui.home.afterwork.model

import androidx.compose.runtime.Stable
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Stable
data class AfterWorkUiState(
    val today: LocalDate = LocalDate.now(),
    val location: String = "모아주식회사",
    val month: Int = 1,
    val todaySalary: Long = 132000L,
    val accumulatedSalary: String = "123,123,000",
    val startHour: Int = 9,
    val startMinute: Int = 0,
    val endHour: Int = 18,
    val endMinute: Int = 0,
    val isOnVacation: Boolean = false,
    val showMoreWorkBottomSheet: Boolean = false,
    val showConfetti: Boolean = true,
) {
    val dateDisplay: String
        get() = today.format(DateTimeFormatter.ofPattern("M월 d일 (E)", Locale.KOREAN))

    val todaySalaryDisplay: String
        get() = formatCurrency(todaySalary)

    val todaySalaryWithPlusDisplay: String
        get() = "+${formatCurrency(todaySalary)}원"

    val startTimeDisplay: String
        get() = "${startHour.toString().padStart(2, '0')}:${startMinute.toString().padStart(2, '0')}"

    val endTimeDisplay: String
        get() = "${endHour.toString().padStart(2, '0')}:${endMinute.toString().padStart(2, '0')}"

    val workTimeDisplay: String
        get() = "$startTimeDisplay - $endTimeDisplay"

    private fun formatCurrency(amount: Long): String {
        return String.format("%,d", amount)
    }
}