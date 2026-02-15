package com.moa.app.core.extensions

import java.text.DecimalFormat

fun Int.toCurrencyFormat(): String {
    val formatter = DecimalFormat("#,###")
    return formatter.format(this)
}
