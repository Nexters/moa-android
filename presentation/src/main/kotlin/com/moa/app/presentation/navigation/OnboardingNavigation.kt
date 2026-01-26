package com.moa.app.presentation.navigation

import com.moa.app.presentation.ui.onboarding.OnboardingNavigationArgs
import kotlinx.serialization.Serializable

@Serializable
sealed interface OnboardingNavigation : RootNavigation {
    @Serializable
    data object Login : OnboardingNavigation

    @Serializable
    @JvmInline
    value class Nickname(
        val args: NicknameNavigationArgs = NicknameNavigationArgs()
    ) : OnboardingNavigation {
        @Serializable
        data class NicknameNavigationArgs(
            val isOnboarding: Boolean = true,
            val nickName: String = "",
        )
    }

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

    data object Back : OnboardingNavigation
}