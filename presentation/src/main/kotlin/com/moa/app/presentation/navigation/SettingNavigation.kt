package com.moa.app.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface SettingNavigation : RootNavigation {
    @Serializable
    data object SettingMenu : SettingNavigation

    @Serializable
    data object MyInfo : SettingNavigation

    @Serializable
    data object Terms : SettingNavigation
}