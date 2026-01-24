package com.moa.app.presentation.ui.onboarding

import com.moa.app.presentation.model.SalaryType
import kotlinx.serialization.Serializable

@Serializable
data class OnboardingNavigationArgs(
    val nickName: String = "",
    val workPlace: String = "",
    val salaryType: SalaryType = SalaryType.Monthly,
    val salary: String = "",
)
