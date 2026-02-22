package com.moa.salary.app.core.extensions

import com.moa.salary.app.core.util.PayrollConstants
import java.util.Locale

fun makeTimeString(hour: Int, minute: Int): String {
    return String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
}

fun makeDateString(year: Int, month: Int, day: Int): String {
    return String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, day)
}

fun formatCurrency(amount : Long) : String {
    return String.format(Locale.getDefault(), "%,d", amount)
}

fun String.makePriceString(): String {
    val value = this.toDoubleOrNull() ?: return ""

    if (value < PayrollConstants.MAN) {
        return ""
    }

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

fun String.toHourMinute(): Pair<Int, Int> {
    val parts = this.split(":")
    require(parts.size >= 2) { "Invalid time format: $this" }

    val hour = parts[0].toInt()
    val minute = parts[1].toInt()

    return Pair(hour, minute)
}

fun String.toHourMinuteOrNull(): Pair<Int, Int>? {
    return try {
        val parts = this.split(":")
        if (parts.size >= 2) {
            Pair(parts[0].toInt(), parts[1].toInt())
        } else null
    } catch (_: Exception) {
        null
    }
}

fun Char.isKoreanEnglishOrDigit(): Boolean {
    return this in 'a'..'z' ||
            this in 'A'..'Z' ||
            this in '0'..'9' ||                 // 숫자 추가
            this in '\uAC00'..'\uD7AF' ||       // 완성형 한글
            this in '\u1100'..'\u11FF' ||       // 한글 자음/모음 (조합 중)
            this in '\u3130'..'\u318F'          // 한글 호환 자모
}