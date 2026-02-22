package com.moa.salary.app.presentation.ui.home.beforework.model

import androidx.compose.runtime.Stable
import com.moa.salary.app.core.extensions.formatCurrency
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Stable
data class BeforeWorkUiState(
    val today: LocalDate = LocalDate.now(),
    val location: String? = null,
    val workedEarnings: Long = 0L,
    val standardSalary: Long = 0L,
    val dailyPay: Long = 0L,
    val todayEarnedSalary: Long? = null,
    val startHour: Int = 9,
    val startMinute: Int = 0,
    val endHour: Int = 18,
    val endMinute: Int = 0,
    val showTimeBottomSheet: Boolean = false,
    val isWorkDay: Boolean = true,
    val isOnVacation: Boolean = false,
) {
    val accumulatedSalary: String
        get() = formatCurrency(workedEarnings)

    val todaySalary: String
        get() = "${formatCurrency(dailyPay)}원"

    val todayEarnedSalaryDisplay: String?
        get() = todayEarnedSalary?.let { "+${formatCurrency(it)}" }

    val dateDisplay: String
        get() = today.format(DateTimeFormatter.ofPattern("M월 d일 (E)", Locale.KOREAN))

    val month: Int
        get() = today.monthValue

    val workTimeDisplay: String
        get() = if (isOnVacation) {
            "휴가"
        } else {
            "${startHour.toString().padStart(2, '0')}:${
                startMinute.toString().padStart(2, '0')
            } - ${endHour.toString().padStart(2, '0')}:${endMinute.toString().padStart(2, '0')}"
        }

    val autoClockInTime: String
        get() = "${startHour.toString().padStart(2, '0')}:${
            startMinute.toString().padStart(2, '0')
        }"

    val additionalSalary: Long?
        get() = if (workedEarnings > standardSalary) workedEarnings - standardSalary else null

    val additionalSalaryDisplay: String?
        get() = additionalSalary?.let { String.format(Locale.getDefault(), "+%,d원", it) }
}