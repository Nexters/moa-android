package com.moa.salary.app.core.model.setting

import androidx.compose.runtime.Stable

@Stable
sealed interface NotificationSetting {
    @JvmInline
    value class Title(val title: String) : NotificationSetting

    data class Content(
        val type: String,
        val title: String,
        val checked: Boolean,
    ) : NotificationSetting
}
