package com.moa.salary.app.core.extensions

import kotlin.math.roundToInt

fun Long.convertMinutesToRoundedHours(): Int {
    return (this / 60.0).roundToInt()
}