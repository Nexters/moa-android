package com.moa.app.core

import java.util.Locale

fun makeTimeString(hour: Int, minute: Int): String {
    return String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
}