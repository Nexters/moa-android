package com.moa.salary.app.core.model.work

import androidx.compose.runtime.Stable
import com.moa.salary.app.core.extensions.makeTimeString
import com.moa.salary.app.core.model.ImmutableListSerializer
import com.moa.salary.app.core.model.LocalDateTimeSerializer
import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
@Stable
data class Home(
    val workplace: String?,
    val workedEarnings: Long,
    val standardSalary: Long,
    val dailyPay: Long,
    val type: WorkdayType,
    @Serializable(with = ImmutableListSerializer::class)
    val events: ImmutableList<Event>,
    @Serializable(with = LocalDateTimeSerializer::class)
    val clockInDateTime: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val clockOutDateTime: LocalDateTime,
) {
    val startHour: Int get() = clockInDateTime.hour
    val startMinute: Int get() = clockInDateTime.minute
    val endHour: Int get() = clockOutDateTime.hour
    val endMinute: Int get() = clockOutDateTime.minute

    val clockInTime: String get() = makeTimeString(startHour, startMinute)
    val clockOutTime: String get() = makeTimeString(endHour, endMinute)
}
