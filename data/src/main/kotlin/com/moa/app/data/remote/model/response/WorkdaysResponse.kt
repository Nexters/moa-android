package com.moa.app.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class WorkdaysResponse(
    val code: String,
    val message: String,
    val content: List<WorkdayItem>,
)

@Serializable
data class WorkdayItem(
    val date: String,
    val type: String,
)