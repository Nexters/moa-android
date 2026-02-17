package com.moa.app.data.remote.model.request

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class WorkPolicyRequest(
    val effectiveFrom: String = LocalDate.now().toString(),
    val workdays: List<String>,
    val clockInTime: String,
    val clockOutTime: String,
)
