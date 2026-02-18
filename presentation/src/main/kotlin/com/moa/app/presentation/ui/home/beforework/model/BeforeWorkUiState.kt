package com.moa.app.presentation.ui.home.beforework.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class BeforeWorkUiState(
    val today: LocalDate = LocalDate.now(),
    val location: String = "모아주식회사",
    val accumulatedSalary: String = "2,150,000",
    val todayEarnedSalary: Long? = null,
    val todaySalary: String = "150,000원",
    val startHour: Int = 9,
    val startMinute: Int = 0,
    val endHour: Int = 18,
    val endMinute: Int = 0,
    val showTimeBottomSheet: Boolean = false,
    val isWorkDay: Boolean = true,
) {
    val todayEarnedSalaryDisplay: String?
        get() = todayEarnedSalary?.let { "+${String.format("%,d", it)}" }

    val dateDisplay: String
        get() = today.format(DateTimeFormatter.ofPattern("M월 d일 (E)", Locale.KOREAN))

    val month: Int
        get() = today.monthValue

    val workTimeDisplay: String
        get() = "${startHour.toString().padStart(2, '0')}:${startMinute.toString().padStart(2, '0')} - ${endHour.toString().padStart(2, '0')}:${endMinute.toString().padStart(2, '0')}"

    val autoClockInTime: String
        get() = "${startHour.toString().padStart(2, '0')}:${startMinute.toString().padStart(2, '0')}"
}