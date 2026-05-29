package com.moa.salary.app.data.repository

import com.moa.salary.app.data.local.PreferencesDataStore
import java.time.LocalDate
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val preferencesDataStore: PreferencesDataStore,
) : ReviewRepository {
    override suspend fun getClickedSettingReview(): Boolean {
        return preferencesDataStore.getClickedSettingReview()
    }

    override suspend fun setClickedSettingReview() {
        preferencesDataStore.putClickedSettingReview(true)
    }

    override suspend fun incrementHomeVisitCountIfNewDay(): Int {
        val today = LocalDate.now().toString()
        val visitCount = preferencesDataStore.getHomeVisitCount()

        return if (preferencesDataStore.getHomeVisitLastDate() == today) {
            visitCount
        } else {
            preferencesDataStore.putHomeVisitLastDate(today)
            preferencesDataStore.putHomeVisitCount(visitCount + 1)
            visitCount + 1
        }
    }
}
