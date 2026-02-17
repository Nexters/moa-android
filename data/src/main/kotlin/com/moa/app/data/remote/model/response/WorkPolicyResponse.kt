package com.moa.app.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class WorkPolicyResponse(
    val effectiveFrom: String,
    val workdays: List<String>,
    val clockInTime: String,
    val clockOutTime: String,
)
