package com.moa.app.data.repository

import com.moa.app.core.model.onboarding.OnboardingStatus
import com.moa.app.core.model.onboarding.Payroll
import com.moa.app.core.model.onboarding.Term
import com.moa.app.core.model.onboarding.WorkPolicy
import com.moa.app.data.remote.api.OnboardingService
import com.moa.app.data.remote.api.TokenService
import com.moa.app.data.remote.mapper.toData
import com.moa.app.data.remote.mapper.toDomain
import com.moa.app.data.remote.model.request.NicknameRequest
import com.moa.app.data.remote.model.request.TokenRequest
import kotlinx.collections.immutable.ImmutableList
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val tokenService: TokenService,
    private val onboardingService: OnboardingService,
) : OnboardingRepository {
    override suspend fun getOnboardingStatus(): OnboardingStatus {
        return onboardingService.getStatus().toDomain()
    }

    override suspend fun postToken(
        idToken: String,
        fcmDeviceToken: String,
    ): String {
        return tokenService.postToken(
            TokenRequest(
                idToken = idToken,
                fcmDeviceToken = fcmDeviceToken,
            )
        ).accessToken
    }

    override suspend fun patchNickname(nickName: String) {
        onboardingService.patchProfile(NicknameRequest(nickname = nickName))
    }

    override suspend fun patchPayroll(payroll: Payroll) {
        onboardingService.patchPayroll(payroll.toData())
    }

    override suspend fun getTerms(): ImmutableList<Term> {
        return onboardingService.getTerms().terms.toDomain()
    }

    override suspend fun patchWorkPolicy(workPolicy: WorkPolicy) {
        onboardingService.patchWorkPolicy(workPolicy.toData())
    }

    override suspend fun putTerms(terms: ImmutableList<Term>) {
        onboardingService.putAgreements(terms.toData())
    }
}