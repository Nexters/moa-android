package com.moa.salary.app.core.model.history

import kotlinx.serialization.Serializable

@Serializable
data class Workday(
    val date: String,
    val type: WorkdayType,
)

@Serializable
enum class WorkdayType {
    WORK,
    VACATION,
    NONE,
}