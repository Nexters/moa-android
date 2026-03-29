package com.moa.salary.app.core.model.work

import kotlinx.serialization.Serializable

@Serializable
enum class WorkdayType(val value : String) {
    VACATION("휴가"),
    NONE("휴무"),
    WORK("근무"),
}