package com.moa.salary.app.data.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class WorkdayRequest(
    val type: String,
    val clockInTime: String,
    val clockOutTime: String,
)