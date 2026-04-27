package com.moa.salary.app.presentation.model

import com.moa.salary.app.core.model.onboarding.Time
import com.moa.salary.app.core.model.work.WorkdayType
import kotlinx.serialization.Serializable

@Serializable
sealed interface HistoryNavigation : RootNavigation {
    @Serializable
    data object Calendar : HistoryNavigation

    @Serializable
    @JvmInline
    value class ModifyWorkday(
        val args: ModifyWorkdayArgs,
    ) : HistoryNavigation {
        @Serializable
        data class ModifyWorkdayArgs(
            val joinedAt: String?,
            val workdayType: WorkdayType,
            val date: String,
            val time: Time,
        )
    }

    data object Back : HistoryNavigation
}