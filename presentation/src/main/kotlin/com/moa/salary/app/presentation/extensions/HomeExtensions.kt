package com.moa.salary.app.presentation.extensions

import com.moa.salary.app.core.model.work.Home
import com.moa.salary.app.core.model.work.WorkdayType
import com.moa.salary.app.presentation.model.HomeNavigation
import java.time.LocalDate
import java.time.LocalDateTime

fun Home.determineHomeNavigation(completedWorkDay: LocalDate? = null): HomeNavigation {
    val today = LocalDate.now()
    val now = LocalDateTime.now()

    return when {
        type == WorkdayType.NONE -> HomeNavigation.BeforeWork(this)
        !now.isBefore(clockOutDateTime) -> {
            if (completedWorkDay == today) {
                HomeNavigation.AfterWork(this)
            } else {
                HomeNavigation.Working(
                    home = this,
                    showWorkCompletionOverlay = true,
                )
            }
        }

        !now.isBefore(clockInDateTime) -> HomeNavigation.Working(this)
        else -> HomeNavigation.BeforeWork(this)
    }
}
