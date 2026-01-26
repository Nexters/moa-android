package com.moa.app.presentation.ui.onboarding

import com.moa.app.presentation.model.SalaryType
import com.moa.app.presentation.model.Time
import com.moa.app.presentation.model.WorkScheduleDay
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.serialization.Serializable

@Serializable
data class OnboardingNavigationArgs(
    val isOnboarding: Boolean = true,
    val nickName: String = "",
    val workPlace: String = "",
    val salaryType: SalaryType = SalaryType.Monthly,
    val salary: String = "",
    val workScheduleDays: ImmutableSet<WorkScheduleDay> = persistentSetOf(),
    val times: ImmutableList<Time> = persistentListOf(),
)
