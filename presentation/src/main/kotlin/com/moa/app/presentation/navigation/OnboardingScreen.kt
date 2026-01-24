package com.moa.app.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface OnboardingScreen : Screen{
    @Serializable
    data object Login : OnboardingScreen

    @Serializable
    data object Nickname : OnboardingScreen

    @Serializable
    data object WorkPlace : OnboardingScreen

    @Serializable
    data object Salary : OnboardingScreen

    @Serializable
    data object WorkSchedule : OnboardingScreen

    @Serializable
    data object WidgetGuide : OnboardingScreen

    @Serializable
    data object Notification : OnboardingScreen

    data object Back : OnboardingScreen
}