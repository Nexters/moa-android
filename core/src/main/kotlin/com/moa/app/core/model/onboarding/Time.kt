package com.moa.app.core.model.onboarding

import androidx.compose.runtime.Stable
import com.moa.app.core.extensions.makeTimeString
import kotlinx.serialization.Serializable

@Serializable
@Stable
data class Time(
    val startHour: Int,
    val startMinute: Int,
    val endHour: Int,
    val endMinute: Int,
) {
    fun getFormattedTimeRange(): String {
        return "${makeTimeString(startHour, startMinute)}~${makeTimeString(endHour, endMinute)}"
    }
}