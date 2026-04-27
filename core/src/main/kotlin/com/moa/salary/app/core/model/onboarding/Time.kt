package com.moa.salary.app.core.model.onboarding

import androidx.compose.runtime.Stable
import com.moa.salary.app.core.extensions.makeTimeString
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

    fun calculateTimeDiff(): Pair<Int, Int> {
        val totalStartMinutes = startHour * 60 + startMinute
        var totalEndMinutes = endHour * 60 + endMinute

        if (totalEndMinutes < totalStartMinutes) {
            totalEndMinutes += 24 * 60
        }

        val diffInMinutes = totalEndMinutes - totalStartMinutes

        val hours = diffInMinutes / 60
        val minutes = diffInMinutes % 60


        return Pair(hours, minutes)
    }
}