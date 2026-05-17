package com.moa.salary.app.core.model.onboarding

import androidx.compose.runtime.Stable
import com.moa.salary.app.core.extensions.calculateTimeDiffString
import com.moa.salary.app.core.extensions.makeTimeString
import kotlinx.serialization.Serializable
import kotlin.compareTo

@Serializable
@Stable
data class Time(
    val startHour: Int,
    val startMinute: Int,
    val endHour: Int,
    val endMinute: Int,
) {
    val diffTimeString : String = calculateTimeDiffString(
        startHour = startHour,
        startMinute = startMinute,
        endHour = endHour,
        endMinute = endMinute,
    )

    fun getFormattedTimeRange(): String {
        return "${makeTimeString(startHour, startMinute)}~${makeTimeString(endHour, endMinute)}"
    }
}