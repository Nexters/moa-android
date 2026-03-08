package com.moa.salary.app.core.extensions

import java.util.Locale
import kotlin.math.roundToInt

fun Int.convertMinutesToRoundedHours(): Int {
    return (this / 60.0).roundToInt()
}

fun Int.toHourMinuteSecondString(): String {
    val totalSeconds = this.coerceAtLeast(0)

    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds)
}