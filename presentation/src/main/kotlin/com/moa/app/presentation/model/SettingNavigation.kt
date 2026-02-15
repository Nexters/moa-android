package com.moa.app.presentation.model

import kotlinx.serialization.Serializable

sealed interface SettingNavigation : RootNavigation {
    @Serializable
    data object SettingMenu : SettingNavigation

    @Serializable
    data object WorkInfo : SettingNavigation

    @Serializable
    @JvmInline
    value class CompanyName(val companyName: String) : SettingNavigation

    @Serializable
    data object NotificationSetting : SettingNavigation

    @Serializable
    data object Terms : SettingNavigation

    @Serializable
    @JvmInline
    value class SalaryDay(val day: Int) : SettingNavigation

    @Serializable
    data object WithDraw : SettingNavigation

    data object Back : SettingNavigation
}