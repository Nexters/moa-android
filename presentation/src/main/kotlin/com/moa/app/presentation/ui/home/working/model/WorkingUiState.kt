package com.moa.app.presentation.ui.home.working.model

import androidx.compose.runtime.Stable

@Stable
data class WorkingUiState(
    val monthSalary: String = "123,203,000",
    val todaySalary: Long = 0L,
    val elapsedHours: Int = 0,
    val elapsedMinutes: Int = 0,
    val elapsedSeconds: Int = 0,
    val startHour: Int = 9,
    val startMinute: Int = 0,
    val endHour: Int = 18,
    val endMinute: Int = 0,
    val currentHour: Int = 9,
    val currentMinute: Int = 0,
    val currentSecond: Int = 0,
    val showTimeBottomSheet: Boolean = false,
    val showScheduleAdjustBottomSheet: Boolean = false,
    val workStatus: WorkStatus = WorkStatus.WORKING,
    val currentTooltipIndex: Int = 0,
    val remainingHours: Int = 3,
    val isOnVacation: Boolean = false,
    val showConfetti: Boolean = false,
    val showWorkCompletionOverlay: Boolean = false,
    val showMoreWorkBottomSheet: Boolean = false,
    val showWorkTimeEditBottomSheet: Boolean = false,
) {
    val elapsedTimeDisplay: String
        get() = "${elapsedHours}:${elapsedMinutes.toString().padStart(2, '0')}:${elapsedSeconds.toString().padStart(2, '0')}"

    val startTimeDisplay: String
        get() = "${startHour.toString().padStart(2, '0')}:${startMinute.toString().padStart(2, '0')}"

    val endTimeDisplay: String
        get() = "${endHour.toString().padStart(2, '0')}:${endMinute.toString().padStart(2, '0')}"

    val progress: Float
        get() {
            val startTotalSeconds = startHour * 3600 + startMinute * 60
            val endTotalSeconds = endHour * 3600 + endMinute * 60
            val currentTotalSeconds = currentHour * 3600 + currentMinute * 60 + currentSecond

            val totalWorkSeconds = endTotalSeconds - startTotalSeconds
            val elapsedFromStart = currentTotalSeconds - startTotalSeconds

            return if (totalWorkSeconds > 0) {
                (elapsedFromStart.toFloat() / totalWorkSeconds).coerceIn(0f, 1f)
            } else {
                0f
            }
        }

    val coinHeightFraction: Float
        get() {
            if (showWorkCompletionOverlay) return 0.7f

            val totalSeconds = elapsedSeconds
            val fractionInCycle = totalSeconds / 60f
            val minHeight = 0.3f
            val maxHeight = 0.7f
            return minHeight + (maxHeight - minHeight) * fractionInCycle
        }

    val todaySalaryDisplay: String
        get() = formatCurrency(todaySalary)

    private fun formatCurrency(amount: Long): String {
        return String.format("%,d", amount)
    }
}

enum class WorkStatus {
    WORKING,
    LUNCH_TIME,
    VACATION,
    OVERTIME,
}