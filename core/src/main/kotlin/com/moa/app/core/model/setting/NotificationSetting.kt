package com.moa.app.core.model.setting

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Stable
@Serializable
sealed class NotificationSetting {
    abstract val id: NotificationId
    abstract val title: String
    abstract val enabled: Boolean

    @Stable
    @Serializable
    data class Service(
        override val id: NotificationId,
        override val title: String,
        override val enabled: Boolean,
    ) : NotificationSetting()

    @Stable
    @Serializable
    data class Marketing(
        override val id: NotificationId,
        override val title: String,
        override val enabled: Boolean,
    ) : NotificationSetting()
}

@Serializable
enum class NotificationId {
    COMMUTE,
    SALARY_DAY,
    BENEFITS,
}
