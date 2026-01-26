package com.moa.app.core

import java.util.Locale

fun makeTimeString(hour: Int, minute: Int): String {
    return String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
}

fun String.makePriceString(): String {
    val value = this.toDoubleOrNull() ?: return ""
    val divided = value / 10_000
    val formatted = if (divided % 1.0 == 0.0) {
        divided.toInt().toString()
    } else {
        String.format(Locale.getDefault(), "%.1f", divided)
    }
    return "${formatted}만원"
}