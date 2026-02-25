package com.moa.salary.app.core.extensions

import kotlin.math.roundToInt

fun Int.convertMinutesToRoundedHours(): Int {
    return (this / 60.0).roundToInt()
}