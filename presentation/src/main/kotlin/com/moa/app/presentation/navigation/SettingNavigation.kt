package com.moa.app.presentation.navigation

import com.moa.app.presentation.model.SalaryType
import com.moa.app.presentation.model.WorkScheduleDay
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.serialization.Serializable

sealed interface SettingNavigation : RootNavigation {
    @Serializable
    data object SettingMenu : SettingNavigation

    @Serializable
    @JvmInline
    value class WorkInfo(
        val args: WorkInfoArgs,
    ) : SettingNavigation {
        @Serializable
        data class WorkInfoArgs(
            val oauthType: String,
            val salaryType: SalaryType,
            val salary: String,
            val salaryDate: Int,
            val workPlace: String,
            val workScheduleDays: ImmutableSet<WorkScheduleDay>,
            val workStartTime: String,
            val workEndTime: String,
            val lunchStartTime: String,
            val lunchEndTime: String,
        )
    }

    @Serializable
    data object NotificationSetting : SettingNavigation

    @Serializable
    data object Terms : SettingNavigation

    @Serializable
    data object SalaryDate : SettingNavigation

    data object Back : SettingNavigation
}