package com.moa.app.data.repository

import com.moa.app.core.model.onboarding.OnboardingStatus

interface OnboardingRepository {
    suspend fun getOnboardingStatus(): OnboardingStatus
    suspend fun getRandomNickName(): String
}