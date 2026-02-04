package com.moa.app.core.model

import com.moa.app.core.extensions.makeTimeString
import kotlinx.serialization.Serializable

@Serializable
sealed interface Time {
    val title: String
    val description: String
    val startHour: Int
    val startMinute: Int
    val endHour: Int
    val endMinute: Int
    val startButtonText: String
    val endButtonText: String

    @Serializable
    data class Work(
        override val title: String = "근무 시간",
        override val description: String = "",
        override val startHour: Int,
        override val startMinute: Int,
        override val endHour: Int,
        override val endMinute: Int,
        override val startButtonText: String = "확인",
        override val endButtonText: String = "확인",
    ) : Time

    @Serializable
    data class Lunch(
        override val title: String = "점심 시간",
        override val description: String = "",
        override val startHour: Int,
        override val startMinute: Int,
        override val endHour: Int,
        override val endMinute: Int,
        override val startButtonText: String = "확인",
        override val endButtonText: String = "확인",
    ) : Time

    fun getFormattedTimeRange(): String {
        return "${makeTimeString(startHour, startMinute)}~${makeTimeString(endHour, endMinute)}"
    }
}