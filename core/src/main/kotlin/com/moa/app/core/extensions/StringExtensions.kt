package com.moa.app.core.extensions

import java.util.Locale

fun makeTimeString(hour: Int, minute: Int): String {
    return String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
}

fun String.makePriceString(): String {
    val value = this.toDoubleOrNull() ?: return ""

    val EOK = 100_000_000
    val MAN = 10_000

    if (value < EOK) {
        val divided = value / MAN
        var formatted = String.format(Locale.getDefault(), "%.1f", divided)
        if (formatted.endsWith(".0")) {
            formatted = formatted.substring(0, formatted.length - 2)
        }
        return "${formatted}만원"
    }

    else {
        val valueAsLong = value.toLong()
        val eokPart = valueAsLong / EOK
        val manPart = (valueAsLong % EOK) / MAN

        return buildString {
            append("${eokPart}억")
            if (manPart > 0) {
                append(" ${manPart}만원")
            }
        }
    }
}