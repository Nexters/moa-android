package com.moa.app.presentation.navigation

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
    data object Home : RootNavigation

    @Serializable
    data object History : RootNavigation

    @Serializable
    data object Setting : RootNavigation

    // TODO 추후 Webview
    @Serializable
    @JvmInline
    value class Webview(val url: String) : RootNavigation

    data object Back : RootNavigation
}