package com.moa.salary.app.core.model.work

import androidx.compose.runtime.Stable
import com.moa.salary.app.core.model.onboarding.Time
import kotlinx.serialization.Serializable

@Serializable
@Stable
data class Schedule(
    val id: Long,
    val date: LocalDateModel,
    val type: ScheduleType,
    val time: Time? = null,
    val amount: Long? = null,
)

@Serializable
@Stable
data class LocalDateModel(
    val year: Int,
    val month: Int,
    val day: Int,
) {
    fun toDisplayString(): String = "${year}년 ${month}월 ${day}일"

    companion object {
        fun today(): LocalDateModel {
            val now = java.time.LocalDate.now()
            return LocalDateModel(now.year, now.monthValue, now.dayOfMonth)
        }
    }
}