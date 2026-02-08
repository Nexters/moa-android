package com.moa.app.core.model.setting

import com.moa.app.core.model.onboarding.Payroll
import com.moa.app.core.model.onboarding.WorkPolicy

data class WorkInfo(
    val oAuthType: OAuthType?,
    val payroll: Payroll,
    val workPlace: String,
    val workPolicy: WorkPolicy,
)
