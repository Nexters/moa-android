package com.moa.app.core.model

data class OnboardingState(
    val loginCompleted: Boolean,
    val nickNameCompleted: Boolean,
    val workPlaceCompleted: Boolean,
    val salaryCompleted: Boolean,
    val workScheduleCompleted: Boolean,
)
