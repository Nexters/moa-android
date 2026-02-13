package com.moa.app.core.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeUtil {
    fun getCurrentTimeKorean(): String {
        val dateFormat = SimpleDateFormat("a hh:mm", Locale.KOREAN)
        return dateFormat.format(Date())
    }
}
