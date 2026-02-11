package com.moa.app.core.model.widget

sealed interface Widget {
    data class Working(
        val daySalary: String,
        val time: String,
    ) : Widget

    data class Vacation(
        val daySalary: String,
        val time: String,
    ) : Widget

    @JvmInline
    value class Finish(val monthlySalary: String) : Widget
}