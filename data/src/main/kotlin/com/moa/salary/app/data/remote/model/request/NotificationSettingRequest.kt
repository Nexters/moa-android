package com.moa.salary.app.data.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class NotificationSettingRequest(
    val type: String,
    val checked: Boolean,
)
