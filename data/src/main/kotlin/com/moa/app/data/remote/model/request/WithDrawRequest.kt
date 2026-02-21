package com.moa.app.data.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class WithDrawRequest(
    val reason: List<String>,
)
