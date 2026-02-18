package com.moa.app.data.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class WorkplaceRequest(
    val workplace: String,
)
