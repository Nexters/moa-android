package com.moa.app.presentation.model

sealed class Time(
    open val title: String,
    open val description : String,
    open val startHour: Int,
    open val startMinute: Int,
    open val endHour: Int,
    open val endMinute: Int,
    open val startButtonText : String,
    open val endButtonText : String
) {
    data class Work(
        override val title: String = "근무 시간",
        override val description : String = "",
        override val startHour: Int,
        override val startMinute: Int,
        override val endHour: Int,
        override val endMinute: Int,
        override val startButtonText : String = "확인",
        override val endButtonText : String = "확인",
    ) : Time(
        title = title,
        description = description,
        startHour = startHour,
        startMinute = startMinute,
        endHour = endHour,
        endMinute = endMinute,
        startButtonText = startButtonText,
        endButtonText = endButtonText
    )

    data class Lunch(
        override val title: String = "점심 시간",
        override val description : String = "",
        override val startHour: Int,
        override val startMinute: Int,
        override val endHour: Int,
        override val endMinute: Int,
        override val startButtonText : String = "확인",
        override val endButtonText : String = "확인",
    ) : Time(
        title = title,
        description = description,
        startHour = startHour,
        startMinute = startMinute,
        endHour = endHour,
        endMinute = endMinute,
        startButtonText = startButtonText,
        endButtonText = endButtonText
    )
}