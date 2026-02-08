package com.moa.app.presentation.model

import com.moa.app.core.model.ImmutableListSerializer
import com.moa.app.core.model.onboarding.Payroll
import com.moa.app.core.model.onboarding.Time
import com.moa.app.core.model.onboarding.WorkPolicy
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
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
    value class Salary(
        val args: SalaryNavigationArgs = SalaryNavigationArgs()
    ) : OnboardingNavigation {
        @Serializable
        data class SalaryNavigationArgs(
            val isOnboarding: Boolean = true,
            val salaryType: Payroll.SalaryType = Payroll.SalaryType.ANNUAL,
            val salary: String = "",
        )
    }

    @Serializable
    @JvmInline
    value class WorkSchedule(
        val args: WorkScheduleNavigationArgs = WorkScheduleNavigationArgs()
    ) : OnboardingNavigation {
        @Serializable
        data class WorkScheduleNavigationArgs(
            val isOnboarding: Boolean = true,
            @Serializable(with = ImmutableListSerializer::class)
            val workScheduleDays: ImmutableList<WorkPolicy.WorkScheduleDay> = persistentListOf(
                WorkPolicy.WorkScheduleDay.MON,
                WorkPolicy.WorkScheduleDay.TUE,
                WorkPolicy.WorkScheduleDay.WED,
                WorkPolicy.WorkScheduleDay.THU,
                WorkPolicy.WorkScheduleDay.FRI,
            ),
            @Serializable(with = ImmutableListSerializer::class)
            val times: ImmutableList<Time> = persistentListOf(
                Time.Work(
                    startHour = 9,
                    startMinute = 0,
                    endHour = 18,
                    endMinute = 0,
                ),
                Time.Lunch(
                    startHour = 12,
                    startMinute = 0,
                    endHour = 13,
                    endMinute = 0,
                )
            ),
        )
    }

    @Serializable
    data object WidgetGuide : OnboardingNavigation

    data object Back : OnboardingNavigation
}