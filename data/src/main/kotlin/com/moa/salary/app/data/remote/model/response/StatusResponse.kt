package com.moa.salary.app.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class StatusResponse(
    val profile: ProfileResponse?,
    val payroll: PayrollResponse?,
    val workPolicy: WorkPolicyResponse?,
    val hasRequiredTermsAgreed: Boolean,
)
