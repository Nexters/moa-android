package com.moa.app.data.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class WorkPolicyRequest(
    val workdays: List<String>,
    val clockInTime: String,
    val clockOutTime: String,
)
