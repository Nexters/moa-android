package com.moa.app.core.model.onboarding

import kotlinx.collections.immutable.ImmutableList

data class WorkPolicy(
    val workScheduleDays: ImmutableList<WorkScheduleDay>,
    val time: Time
) {
    enum class WorkScheduleDay(val day: String) {
        MON("월"),
        TUE("화"),
        WED("수"),
        THU("목"),
        FRI("금"),
        SAT("토"),
        SUN("일")
    }
}
