package com.moa.salary.app.core.model.work

import androidx.compose.runtime.Stable
import com.moa.salary.app.core.extensions.makeTimeString
import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class Workday(
    val date: String,
    val type: WorkdayType,
    val status: WorkdayStatus,
    val events: ImmutableList<Event>,
    val dailyPay: Long,
    val startHour: Int?,
    val startMinute: Int?,
    val endHour: Int?,
    val endMinute: Int?,
) {
    val clockInTime: String = makeTimeString(startHour ?: 9, startMinute ?: 0)
    val clockOutTime: String = makeTimeString(endHour ?: 18, endMinute ?: 0)
}