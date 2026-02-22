package com.moa.app.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class WorkdayDetailResponse(
    val code: String,
    val message: String,
    val content: WorkdayDetailContent,
)

@Serializable
data class WorkdayDetailContent(
    val date: String,
    val type: String,
    val clockInTime: String?,
    val clockOutTime: String?,
)