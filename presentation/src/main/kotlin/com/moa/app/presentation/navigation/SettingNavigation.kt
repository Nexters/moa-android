package com.moa.app.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface SettingNavigation : RootNavigation {
    @Serializable
    data object SettingMenu : SettingNavigation

    @Serializable
    data object WorkInfo : SettingNavigation

    @Serializable
    data object NotificationSetting : SettingNavigation

    @Serializable
    data object Terms : SettingNavigation

    @Serializable
    data object SalaryDate : SettingNavigation

    @Serializable
    data object WithDraw : SettingNavigation

    data object Back : SettingNavigation
}