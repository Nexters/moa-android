package com.moa.app.core.model.widget

import kotlinx.serialization.Serializable

@Serializable
sealed interface Widget {
    @Serializable
    data class Working(
        val daySalary: String,
        val time: String,
    ) : Widget

    @Serializable
    data class Vacation(
        val daySalary: String,
        val time: String,
    ) : Widget

    @Serializable
    data class Finish(val monthlySalary: String) : Widget
}