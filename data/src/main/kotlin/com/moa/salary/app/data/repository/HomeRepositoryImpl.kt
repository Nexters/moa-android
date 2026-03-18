package com.moa.salary.app.data.repository

import com.moa.salary.app.core.extensions.toLocalDateOrNull
import com.moa.salary.app.core.model.work.Home
import com.moa.salary.app.data.local.PreferencesDataStore
import com.moa.salary.app.data.remote.api.MoaService
import com.moa.salary.app.data.remote.mapper.toDomain
import java.time.LocalDate
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val moaService: MoaService,
    private val preferencesDataStore: PreferencesDataStore,
) : HomeRepository {
    override suspend fun getHome(): Home {
        return moaService.getHome().toDomain()
    }

    override suspend fun getShownNotificationBottomSheet(): Boolean {
        return preferencesDataStore.getShownNotificationBottomSheet()
    }

    override suspend fun putShownNotificationBottomSheet(value: Boolean) {
        preferencesDataStore.putShownNotificationBottomSheet(value)
    }

    override suspend fun getCompletedWorkDay(): LocalDate? {
        return preferencesDataStore.getCompletedWorkDay().toLocalDateOrNull()
    }

    override suspend fun putCompletedWorkDay(completedWorkDay: LocalDate) {
        preferencesDataStore.putCompletedWorkDay(completedWorkDay.toString())
    }
}