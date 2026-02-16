package com.moa.app.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val nickname: String,
    val workplace: String,
)
