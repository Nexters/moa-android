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
import kotlin.random.Random

class OnboardingRepositoryImpl @Inject constructor(
    private val tokenService: TokenService,
    private val onboardingService: OnboardingService,
) : OnboardingRepository {
    override suspend fun getOnboardingStatus(): OnboardingStatus {
        val response = onboardingService.getStatus()

        // TODO 에러 처리
        if (response.content == null) {
            throw Exception("${response.code} ${response.message}")
        }

        return response.content.toDomain()
    }

    override suspend fun postToken(
        idToken: String,
        fcmDeviceToken: String,
    ): String {
        val response = tokenService.postToken(
            TokenRequest(
                idToken = idToken,
                fcmDeviceToken = fcmDeviceToken,
            )
        )

        // TODO 에러 처리
        if (response.content == null) {
            throw Exception("${response.code} ${response.message}")
        }

        return response.content.accessToken
    }

    override fun getRandomNickName(): String {
        return "집계사장${Random.nextInt(10)}"
    }

    override suspend fun patchNickName(nickName: String) {
        val response = onboardingService.patchProfile(ProfileRequest(nickname = nickName))

        // TODO 에러 처리
        if (response.content == null) {
            throw Exception("${response.code} ${response.message}")
        }
    }

    override suspend fun patchPayroll(payroll: Payroll) {
        val response = onboardingService.patchPayroll(
            PayrollRequest(
                salaryInputType = payroll.salaryType.name,
                salaryAmount = payroll.salary.toLong(),
                paydayDay = payroll.paydayDay,
            )
        )

        // TODO 에러 처리
        if (response.content == null) {
            throw Exception("${response.code} ${response.message}")
        }
    }

    override suspend fun getTerms(): ImmutableList<Term> {
        val response = onboardingService.getTerms()

        // TODO 에러 처리
        if (response.content == null) {
            throw Exception("${response.code} ${response.message}")
        }

        return response.content.terms.toDomain()
    }

    override suspend fun patchWorkPolicy(workPolicy: WorkPolicy) {
        val response = onboardingService.patchWorkPolicy(
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

        // TODO 에러 처리
        if (response.content == null) {
            throw Exception("${response.code} ${response.message}")
        }
    }

    override suspend fun putTerms(terms: ImmutableList<Term>) {
        val response = onboardingService.putAgreements(
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

        // TODO 에러 처리
        if (response.content == null) {
            throw Exception("${response.code} ${response.message}")
        }
    }
}