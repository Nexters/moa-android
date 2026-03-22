package com.moa.salary.app.core.extensions

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.toYearMonthDayString(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    return this.format(formatter)
}

fun String?.toLocalDateOrNull(): LocalDate? {
    return runCatching {
        LocalDate.parse(this)
    }.getOrNull()
}

fun String.toLocalDate(): LocalDate =
    LocalDate.parse(this)