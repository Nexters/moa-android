package com.moa.app.presentation.ui.home.afterwork.model

sealed interface AfterWorkIntent {
    data object ClickCheckWorkHistory : AfterWorkIntent
    data object ClickMoreWork : AfterWorkIntent
    data object ClickComplete : AfterWorkIntent
    data object DismissMoreWorkBottomSheet : AfterWorkIntent
    data object DismissConfetti : AfterWorkIntent
    data class ConfirmMoreWork(
        val endHour: Int,
        val endMinute: Int,
    ) : AfterWorkIntent
}