package com.moa.salary.app.core.model.onboarding

data class OnboardingStatus(
    val profile: Profile?,
    val payroll: Payroll?,
    val workPolicy: WorkPolicy?,
    val hasRequiredTermsAgreed: Boolean,
)