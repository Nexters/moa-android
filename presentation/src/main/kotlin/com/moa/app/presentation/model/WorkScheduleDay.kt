package com.moa.app.presentation.model

import kotlinx.serialization.Serializable

@Serializable
enum class WorkScheduleDay(val title: String) {
    MONDAY("월"),
    TUESDAY("화"),
    WEDNESDAY("수"),
    THURSDAY("목"),
    FRIDAY("금"),
    SATURDAY("토"),
    SUNDAY("일"),
}