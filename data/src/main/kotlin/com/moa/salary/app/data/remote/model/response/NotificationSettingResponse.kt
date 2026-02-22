package com.moa.salary.app.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class NotificationSettingResponse(
    val type: String,
    val category: String,
    val title: String,
    val checked: Boolean,
)
