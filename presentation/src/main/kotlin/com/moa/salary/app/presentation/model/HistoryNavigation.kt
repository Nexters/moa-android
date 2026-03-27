package com.moa.salary.app.presentation.model

import com.moa.salary.app.core.model.calendar.Schedule
import kotlinx.serialization.Serializable

@Serializable
sealed interface HistoryNavigation : RootNavigation {
    @Serializable
    data object Calendar : HistoryNavigation

    @Serializable
    @JvmInline
    value class ModifySchedule(
        val args: ModifyScheduleArgs,
    ) : HistoryNavigation {
        @Serializable
        data class ModifyScheduleArgs(
            val currentDate: String,
            val joinedAt : String,
            val schedule: Schedule,
        )
    }

    data object Back : HistoryNavigation
}