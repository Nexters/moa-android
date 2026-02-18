package com.moa.app.data.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class TokenRequest(
    val idToken: String,
    val fcmDeviceToken: String,
)