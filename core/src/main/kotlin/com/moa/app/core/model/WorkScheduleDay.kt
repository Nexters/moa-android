package com.moa.app.core.model

import kotlinx.serialization.Serializable

@Serializable
enum class WorkScheduleDay(val title: String) {
    Monday("월"),
    Tuesday("화"),
    Wednesday("수"),
    Thursday("목"),
    Friday("금"),
    Saturday("토"),
    Sunday("일"),
}