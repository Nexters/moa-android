package com.moa.app.data.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateWorkdayRequest(
    val type: String,
    val clockInTime: String?,
    val clockOutTime: String?,
)