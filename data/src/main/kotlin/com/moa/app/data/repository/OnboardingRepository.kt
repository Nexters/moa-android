package com.moa.app.data.repository

import com.moa.app.core.model.OnboardingState

interface OnboardingRepository {
    suspend fun fetchOnboardingState(): OnboardingState
}