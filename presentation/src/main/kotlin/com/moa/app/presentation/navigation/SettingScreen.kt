package com.moa.app.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface SettingScreen : Screen {
    @Serializable
    data object SettingMenu : SettingScreen

    @Serializable
    data object MyInfo : SettingScreen

    @Serializable
    data object Terms : SettingScreen
}