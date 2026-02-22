package com.moa.app.data.remote.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class ClockOutRequest(
    val clockOutTime: String,
)