package com.moa.app.core.extensions

import com.moa.app.core.util.PayrollConstants
import java.util.Locale

fun makeTimeString(hour: Int, minute: Int): String {
    return String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
}

fun String.makePriceString(): String {
    val value = this.toDoubleOrNull() ?: return ""

    if (value < PayrollConstants.EOK) {
        val divided = value / PayrollConstants.MAN
        var formatted = String.format(Locale.getDefault(), "%.1f", divided)
        if (formatted.endsWith(".0")) {
            formatted = formatted.substring(0, formatted.length - 2)
        }
        return "${formatted}만원"
    } else {
        val valueAsLong = value.toLong()
        val eokPart = valueAsLong / PayrollConstants.EOK
        val manPart = (valueAsLong % PayrollConstants.EOK) / PayrollConstants.MAN

        return buildString {
            append("${eokPart}억")
            if (manPart > 0) {
                append(" ${manPart}만원")
            }
        }
    }
}