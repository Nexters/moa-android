package com.moa.salary.app.core.model.work

import kotlinx.serialization.Serializable

@Serializable
enum class WorkdayType(val value : String) {
    WORK("근무"),
    VACATION("연차"),
    NONE("휴무"),
}