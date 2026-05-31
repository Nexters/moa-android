package com.moa.salary.app.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val userId: Int,
    val accessToken: String,
)
