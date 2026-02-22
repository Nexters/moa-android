package com.moa.salary.app.data.repository

import com.moa.salary.app.core.model.onboarding.OnboardingStatus
import com.moa.salary.app.core.model.onboarding.Payroll
import com.moa.salary.app.core.model.onboarding.Term
import com.moa.salary.app.core.model.onboarding.WorkPolicy
import com.moa.salary.app.data.remote.api.OnboardingService
import com.moa.salary.app.data.remote.mapper.toData
import com.moa.salary.app.data.remote.mapper.toDomain
import com.moa.salary.app.data.remote.mapper.toOnboardingTermDomain
import com.moa.salary.app.data.remote.model.request.NicknameRequest
import kotlinx.collections.immutable.ImmutableList
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val onboardingService: OnboardingService,
) : OnboardingRepository {
    override suspend fun getOnboardingStatus(): OnboardingStatus {
        return onboardingService.getStatus().toDomain()
    }

    override suspend fun patchNickname(nickName: String) {
        onboardingService.patchProfile(NicknameRequest(nickname = nickName))
    }

    override suspend fun patchPayroll(payroll: Payroll) {
        onboardingService.patchPayroll(payroll.toData())
    }

    override suspend fun getTerms(): ImmutableList<Term> {
        return onboardingService.getTerms().terms.toOnboardingTermDomain()
    }

    override suspend fun patchWorkPolicy(workPolicy: WorkPolicy) {
        onboardingService.patchWorkPolicy(workPolicy.toData())
    }

    override suspend fun putTerms(terms: ImmutableList<Term>) {
        onboardingService.putAgreements(terms.toData())
    }
}