package com.moa.app.presentation.ui.home.beforework.model

sealed interface BeforeWorkIntent {
    data object ClickWorkTime : BeforeWorkIntent
    data object ClickEarlyClockIn : BeforeWorkIntent
    data object ClickVacation : BeforeWorkIntent
    data object ClickClockInOnDayOff : BeforeWorkIntent
    data object DismissTimeBottomSheet : BeforeWorkIntent
    data class UpdateWorkTime(
        val startHour: Int,
        val startMinute: Int,
        val endHour: Int,
        val endMinute: Int,
    ) : BeforeWorkIntent
}