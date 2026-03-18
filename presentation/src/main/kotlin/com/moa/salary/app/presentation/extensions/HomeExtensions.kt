package com.moa.salary.app.presentation.extensions

import com.moa.salary.app.core.model.work.Home
import com.moa.salary.app.core.model.work.WorkdayType
import com.moa.salary.app.presentation.model.HomeNavigation
import java.time.LocalDate
import java.time.LocalTime

fun Home.determineHomeNavigation(completedWorkDay: LocalDate? = null): HomeNavigation {
    val today = LocalDate.now()
    val now = LocalTime.now()
    val startTime = LocalTime.of(startHour, startMinute)
    val endTime = LocalTime.of(endHour, endMinute)

    return when {
        type == WorkdayType.NONE -> HomeNavigation.BeforeWork(this)
        now.isAfter(endTime) -> {
            if (completedWorkDay == today) {
                HomeNavigation.AfterWork(this)
            } else {
                HomeNavigation.Working(
                    home = this,
                    showWorkCompletionOverlay = true,
                )
            }
        }

        now.isAfter(startTime) -> HomeNavigation.Working(this)
        else -> HomeNavigation.BeforeWork(this)
    }
}