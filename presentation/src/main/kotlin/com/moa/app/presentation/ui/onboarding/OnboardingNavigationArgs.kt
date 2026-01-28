package com.moa.app.presentation.ui.onboarding

import com.moa.app.core.ImmutableListSerializer
import com.moa.app.presentation.model.SalaryType
import com.moa.app.presentation.model.Time
import com.moa.app.presentation.model.WorkScheduleDay
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
data class OnboardingNavigationArgs(
    val isOnboarding: Boolean = true,
    val nickName: String = "",
    val workPlace: String = "",
    val salaryType: SalaryType = SalaryType.Monthly,
    val salary: String = "",
    @Serializable(with = ImmutableListSerializer::class)
    val workScheduleDays: ImmutableList<WorkScheduleDay> = persistentListOf(),
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
