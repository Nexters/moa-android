package com.moa.app.presentation.navigation

import com.moa.app.presentation.ui.onboarding.OnboardingNavigationArgs
import kotlinx.serialization.Serializable

sealed interface OnboardingNavigation : RootNavigation {
    @Serializable
    data object Login : OnboardingNavigation

    @Serializable
    data object Nickname : OnboardingNavigation

    @Serializable
    @JvmInline
    value class WorkPlace(val args: OnboardingNavigationArgs) : OnboardingNavigation

    @Serializable
    @JvmInline
    value class Salary(val args: OnboardingNavigationArgs) : OnboardingNavigation

    @Serializable
    @JvmInline
    value class WorkSchedule(val args: OnboardingNavigationArgs) : OnboardingNavigation

    @Serializable
    data object WidgetGuide : OnboardingNavigation

    @Serializable
    data object Notification : OnboardingNavigation

    data object Back : OnboardingNavigation
}