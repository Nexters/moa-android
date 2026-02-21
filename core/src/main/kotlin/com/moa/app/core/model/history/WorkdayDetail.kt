package com.moa.app.core.model.history

import kotlinx.serialization.Serializable

@Serializable
data class WorkdayDetail(
    val date: String,
    val type: WorkdayType,
    val clockInTime: String?,
    val clockOutTime: String?,
)