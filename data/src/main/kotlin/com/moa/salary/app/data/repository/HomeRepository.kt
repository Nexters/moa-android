package com.moa.salary.app.data.repository

import com.moa.salary.app.core.model.work.Home

interface HomeRepository {
    suspend fun getHome(): Home
    suspend fun getShownNotificationBottomSheet(): Boolean
    suspend fun putShownNotificationBottomSheet(value: Boolean)
}