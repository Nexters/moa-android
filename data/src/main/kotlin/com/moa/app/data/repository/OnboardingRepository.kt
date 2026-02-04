package com.moa.app.data.repository

import com.moa.app.core.model.OnboardingState

interface OnboardingRepository {
    suspend fun fetchOnboardingState(): OnboardingState
    suspend fun fetchRandomNickName(): String
    suspend fun putNickName(nickName: String)
    suspend fun putWorkPlace(workPlace: String)
}