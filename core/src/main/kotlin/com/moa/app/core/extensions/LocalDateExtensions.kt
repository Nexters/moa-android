package com.moa.app.core.extensions

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.toYearMonthDayString(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    return this.format(formatter)
}