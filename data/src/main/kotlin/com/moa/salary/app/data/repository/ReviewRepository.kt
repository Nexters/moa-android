package com.moa.salary.app.data.repository

interface ReviewRepository {
    suspend fun getClickedSettingReview(): Boolean
    suspend fun setClickedSettingReview()
    suspend fun incrementHomeVisitCountIfNewDay(): Int
}
