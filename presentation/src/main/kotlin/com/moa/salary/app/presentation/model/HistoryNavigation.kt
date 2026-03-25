package com.moa.salary.app.presentation.model

import com.moa.salary.app.core.model.calendar.Schedule
import kotlinx.serialization.Serializable

@Serializable
sealed interface HistoryNavigation : RootNavigation {
    @Serializable
    data object Calendar : HistoryNavigation

    @Serializable
    data class ScheduleForm(
        val currentDate: String,
        val schedule: Schedule,
    ) : HistoryNavigation

    data object Back : HistoryNavigation
}