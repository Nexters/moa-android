package com.moa.app.data.repository

import com.moa.app.core.model.onboarding.OnboardingStatus
import com.moa.app.core.model.onboarding.Profile
import javax.inject.Inject
import kotlin.random.Random

class OnboardingRepositoryImpl @Inject constructor(

) : OnboardingRepository {
    override suspend fun getOnboardingStatus(): OnboardingStatus {
        return OnboardingStatus(
            profile = null,
            payroll = null,
            workPolicy = null,
            hasRequiredTermsAgreed = false,
        )
    }

    override suspend fun getRandomNickName(): String {
        return "집계사장${Random.nextInt(10)}"
    }

    override suspend fun patchProfile(profile: Profile) {
        // TODO patch profile
    }
}