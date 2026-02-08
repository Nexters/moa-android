package com.moa.app.core.model.onboarding

data class OnboardingStatus(
    val nickName: String?,
    val payroll: Payroll?,
    val workPolicy: WorkPolicy?,
    val hasRequiredTermsAgreed: Boolean,
)