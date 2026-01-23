package com.moa.app.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Screen : NavKey {
    @Serializable
    data object Splash : Screen

    @Serializable
    data object Login : Screen

    @Serializable
    data object Nickname : Screen

    @Serializable
    data object WorkPlace : Screen

    @Serializable
    data object Salary : Screen

    @Serializable
    data object WorkSchedule : Screen

    @Serializable
    data object WidgetGuide : Screen

    @Serializable
    data object Notification : Screen

    @Serializable
    data object Home : Screen

    @Serializable
    data object History : Screen

    @Serializable
    data object Setting : Screen

    // TODO 추후 Webview
    @Serializable
    @JvmInline
    value class Webview(val url: String) : Screen

    data object Back : Screen
}