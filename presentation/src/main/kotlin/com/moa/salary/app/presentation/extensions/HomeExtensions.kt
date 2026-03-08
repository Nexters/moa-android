package com.moa.salary.app.presentation.extensions

import com.moa.salary.app.core.model.work.Home
import com.moa.salary.app.core.model.work.WorkdayType
import com.moa.salary.app.presentation.model.HomeNavigation
import java.time.LocalTime

fun Home.determineHomeNavigation(): HomeNavigation {
    val now = LocalTime.now()
    val startTime = LocalTime.of(startHour, startMinute)
    val endTime = LocalTime.of(endHour, endMinute)

    return when {
        type == WorkdayType.NONE -> HomeNavigation.BeforeWork(this)
        now.isAfter(endTime) -> HomeNavigation.AfterWork(this)
        now.isAfter(startTime) -> HomeNavigation.Working(this)
        else -> HomeNavigation.BeforeWork(this)
    }
}