package com.moa.salary.app.data.repository

import com.moa.salary.app.core.model.work.Home
import com.moa.salary.app.core.model.work.WorkdayType
import com.moa.salary.app.data.local.PreferencesDataStore
import com.moa.salary.app.data.remote.api.MoaService
import com.moa.salary.app.data.remote.mapper.toDomain
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val moaService: MoaService,
    private val preferencesDataStore: PreferencesDataStore,
) : HomeRepository {
    override suspend fun getHome(): Home {
       return  Home(
            workplace = "auctor",
            workedEarnings = 4254,
            standardSalary = 5242,
            dailyPay = 1165,
            type = WorkdayType.VACATION,
            startHour = 9,
            startMinute = 0,
            endHour = 18,
            endMinute = 0

        )
        return moaService.getHome().toDomain()
    }

    override suspend fun getShownNotificationBottomSheet(): Boolean {
        return preferencesDataStore.getShownNotificationBottomSheet()
    }

    override suspend fun putShownNotificationBottomSheet(value: Boolean) {
        preferencesDataStore.putShownNotificationBottomSheet(value)
    }
}