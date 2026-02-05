package com.moa.app.data.repository

import com.moa.app.core.model.onboarding.OnboardingStatus
import com.moa.app.core.model.onboarding.Profile

interface OnboardingRepository {
    suspend fun getOnboardingStatus(): OnboardingStatus
    suspend fun getRandomNickName(): String
    suspend fun patchProfile(profile: Profile)
}