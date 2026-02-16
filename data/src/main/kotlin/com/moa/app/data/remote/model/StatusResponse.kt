package com.moa.app.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class StatusResponse(
    val profile: ProfileResponse?,
    val payroll: PayrollResponse?,
    val workPolicy: WorkPolicyResponse?,
    val hasRequiredTermsAgreed: Boolean,
)
