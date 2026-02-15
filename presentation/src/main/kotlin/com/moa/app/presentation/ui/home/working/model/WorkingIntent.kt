package com.moa.app.presentation.ui.home.working.model

sealed interface WorkingIntent {
    data object ClickAdjustSchedule : WorkingIntent
    data object DismissScheduleAdjustBottomSheet : WorkingIntent
    data object DismissTimeBottomSheet : WorkingIntent
    data object DismissConfetti : WorkingIntent

    data object SelectVacation : WorkingIntent
    data object SelectEndWork : WorkingIntent
    data object SelectAdjustTime : WorkingIntent

    data class UpdateWorkTime(
        val startHour: Int,
        val startMinute: Int,
        val endHour: Int,
        val endMinute: Int,
    ) : WorkingIntent

    data object ClickContinueWorking : WorkingIntent
    data object ClickCompleteWork : WorkingIntent
    data object ClickWorkTimeEdit : WorkingIntent
    data object DismissMoreWorkBottomSheet : WorkingIntent
    data object DismissWorkTimeEditBottomSheet : WorkingIntent
    data object ClickTodayVacation : WorkingIntent
    data class ConfirmMoreWork(val endHour: Int, val endMinute: Int) : WorkingIntent
    data class ConfirmWorkTimeEdit(val endHour: Int, val endMinute: Int) : WorkingIntent
}