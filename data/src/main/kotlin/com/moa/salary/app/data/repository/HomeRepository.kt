package com.moa.salary.app.data.repository

import com.moa.salary.app.core.model.work.Home
import java.time.LocalDate

interface HomeRepository {
    suspend fun getHome(): Home
    suspend fun getShownNotificationBottomSheet(): Boolean
    suspend fun putShownNotificationBottomSheet(value: Boolean)

    suspend fun getCompletedWorkDay(): LocalDate?

    suspend fun putCompletedWorkDay(completedWorkDay: LocalDate)
}