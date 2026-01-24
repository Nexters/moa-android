package com.moa.app.presentation.navigation

import com.moa.app.presentation.ui.onboarding.OnboardingNavigationArgs
import kotlinx.serialization.Serializable

sealed interface OnboardingScreen : Screen {
    @Serializable
    data object Login : OnboardingScreen

    @Serializable
    data object Nickname : OnboardingScreen

    @Serializable
    @JvmInline
    value class WorkPlace(val args: OnboardingNavigationArgs) : OnboardingScreen

    @Serializable
    @JvmInline
    value class Salary(val args: OnboardingNavigationArgs) : OnboardingScreen

    @Serializable
    @JvmInline
    value class WorkSchedule(val args: OnboardingNavigationArgs) : OnboardingScreen

    @Serializable
    data object WidgetGuide : OnboardingScreen

    @Serializable
    data object Notification : OnboardingScreen

    data object Back : OnboardingScreen
}