package com.moa.app.core.model.history

import kotlinx.serialization.Serializable

@Serializable
enum class ScheduleType {
    WORK_SCHEDULED,
    WORK_COMPLETED,
    VACATION,
    PAYDAY,
}