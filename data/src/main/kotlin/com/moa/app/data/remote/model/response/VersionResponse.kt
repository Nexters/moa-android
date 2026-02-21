package com.moa.app.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class VersionResponse(
    val latestVersion: String,
    val minimumVersion: String,
)
