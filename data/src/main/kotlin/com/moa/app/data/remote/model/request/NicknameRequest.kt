package com.moa.app.data.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class NicknameRequest(
    val nickname: String,
)
