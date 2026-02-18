package com.moa.app.data.repository

import com.moa.app.core.extensions.makeTimeString
import com.moa.app.core.model.onboarding.OnboardingStatus
import com.moa.app.core.model.onboarding.Payroll
import com.moa.app.core.model.onboarding.Term
import com.moa.app.core.model.onboarding.WorkPolicy
import com.moa.app.data.remote.api.OnboardingService
import com.moa.app.data.remote.api.TokenService
import com.moa.app.data.remote.mapper.toDomain
import com.moa.app.data.remote.model.request.AgreementRequest
import com.moa.app.data.remote.model.request.AgreementsRequest
import com.moa.app.data.remote.model.request.PayrollRequest
import com.moa.app.data.remote.model.request.ProfileRequest
import com.moa.app.data.remote.model.request.TokenRequest
import com.moa.app.data.remote.model.request.WorkPolicyRequest
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

    override suspend fun patchNickName(nickName: String) {
        onboardingService.patchProfile(ProfileRequest(nickname = nickName))
    }

    override suspend fun patchPayroll(payroll: Payroll) {
        onboardingService.patchPayroll(
            PayrollRequest(
                salaryInputType = payroll.salaryType.name,
                salaryAmount = payroll.salary.toLong(),
            )
        )
    }

    override suspend fun getTerms(): ImmutableList<Term> {
        return onboardingService.getTerms().terms.toDomain()
    }

    override suspend fun patchWorkPolicy(workPolicy: WorkPolicy) {
        onboardingService.patchWorkPolicy(
            WorkPolicyRequest(
                workdays = workPolicy.workScheduleDays.map { it.name },
                clockInTime = makeTimeString(
                    workPolicy.time.startHour,
                    workPolicy.time.startMinute
                ),
                clockOutTime = makeTimeString(
                    workPolicy.time.endHour,
                    workPolicy.time.endMinute
                ),
            )
        )
    }

    override suspend fun putTerms(terms: ImmutableList<Term>) {
        onboardingService.putAgreements(
            AgreementsRequest(
                agreements = terms
                    .filter { it !is Term.All }
                    .map {
                        AgreementRequest(
                            code = it.code,
                            agreed = it.checked,
                        )
                    }
            )
        )
    }
}