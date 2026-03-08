package com.moa.salary.app.core.model.work

import kotlinx.serialization.Serializable

@Serializable
enum class ScheduleType {
    WORK_SCHEDULED,
    WORK_COMPLETED,
    VACATION,
    PAYDAY,
}