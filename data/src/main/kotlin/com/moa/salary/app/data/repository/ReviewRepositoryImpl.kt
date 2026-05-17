package com.moa.salary.app.data.repository

import com.moa.salary.app.data.local.PreferencesDataStore
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

    override suspend fun incrementHomeVisitCount(): Int {
        val next = preferencesDataStore.getHomeVisitCount() + 1
        preferencesDataStore.putHomeVisitCount(next)
        return next
    }
}
