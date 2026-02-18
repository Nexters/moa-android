package com.moa.app.presentation.model

import androidx.navigation3.runtime.NavKey
import com.moa.app.core.model.history.LocalDateModel
import com.moa.app.core.model.history.Schedule
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
    data class ScheduleForm(
        val date: LocalDateModel,
        val schedule: Schedule? = null,
    ) : RootNavigation

    @Serializable
    data object Setting : RootNavigation

    @Serializable
    @JvmInline
    value class Webview(val url: String) : RootNavigation

    data object Back : RootNavigation
}