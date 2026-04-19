package com.moa.salary.app.presentation.model

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface RootNavigation : NavKey {
    @Serializable
    data object Splash : RootNavigation

    @Serializable
    @JvmInline
    value class Onboarding(
        val startDestination: OnboardingNavigation = OnboardingNavigation.Login
    ) : RootNavigation

    @Serializable
    @JvmInline
    value class Home(
        val startDestination: HomeNavigation
    ) : RootNavigation

    @Serializable
    @JvmInline
    value class History(
        val startDestination: HistoryNavigation = HistoryNavigation.Calendar,
    ) : RootNavigation

    @Serializable
    @JvmInline
    value class Setting(
        val startDestination: SettingNavigation = SettingNavigation.SettingMenu,
    ) : RootNavigation

    @Serializable
    @JvmInline
    value class Webview(val url: String) : RootNavigation

    data object Back : RootNavigation
}