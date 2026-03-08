package com.moa.salary.app.core.model.work

import kotlinx.serialization.Serializable

@Serializable
enum class WorkdayType {
    WORK,
    VACATION,
    NONE,
}