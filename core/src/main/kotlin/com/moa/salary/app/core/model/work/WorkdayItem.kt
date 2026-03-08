package com.moa.salary.app.core.model.work

import kotlinx.serialization.Serializable

@Serializable
data class WorkdayItem(
    val date: String,
    val type: WorkdayType,
)