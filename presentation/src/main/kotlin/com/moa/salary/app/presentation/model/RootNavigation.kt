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
    data object History : RootNavigation

//    @Serializable
//    data class ScheduleForm(
//        val date: LocalDateModel,
//        val schedule: Schedule? = null,
//    ) : RootNavigation

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