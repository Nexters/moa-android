package com.moa.app.data.repository

import com.moa.app.core.model.onboarding.OnboardingStatus
import com.moa.app.core.model.onboarding.Payroll
import com.moa.app.core.model.onboarding.Term
import com.moa.app.core.model.onboarding.WorkPolicy
import com.moa.app.data.remote.api.TokenService
import com.moa.app.data.remote.model.TokenRequest
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import javax.inject.Inject
import kotlin.random.Random

class OnboardingRepositoryImpl @Inject constructor(
    private val tokenService: TokenService,
) : OnboardingRepository {
    override suspend fun getOnboardingStatus(): OnboardingStatus {
        return OnboardingStatus(
            nickName = null,
            payroll = null,
            workPolicy = null,
            hasRequiredTermsAgreed = false,
        )
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
            throw Exception("Token authentication failed: ${response.message}")
        }

        return response.content.accessToken
    }

    override fun getRandomNickName(): String {
        return "집계사장${Random.nextInt(10)}"
    }

    override suspend fun patchNickName(nickName: String) {
        // TODO patch nickname
    }

    override suspend fun patchPayroll(payroll: Payroll) {
        // TODO patch payroll 금액 * 10000 해야함
    }

    override suspend fun patchWorkPolicy(workPolicy: WorkPolicy) {
        // TODO patch workpolicy
    }

    override suspend fun getTerms(): ImmutableList<Term> {
        return persistentListOf(
            Term.All(
                title = "전체 동의하기",
                url = "",
                checked = false,
            ),
            Term.Required(
                title = "(필수) 서비스 이용 약관 동의",
                url = "https://www.naver.com",
                checked = false,
            ),
            Term.Required(
                title = "(필수) 테스트 이용 약관 동의",
                url = "https://www.naver.com",
                checked = false,
            ),
            Term.Optional(
                title = "(선택) 서비스 이용 약관 동의",
                url = "https://www.naver.com",
                checked = false,
            ),
            Term.Optional(
                title = "(선택) 테스트 이용 약관 동의",
                url = "https://www.naver.com",
                checked = false,
            ),
        )
    }

    override suspend fun putTerms(terms: ImmutableList<Term>) {
        // TODO put terms
    }
}