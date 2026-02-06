package com.moa.app.data.repository

import com.moa.app.core.model.onboarding.OnboardingStatus
import com.moa.app.core.model.onboarding.Payroll
import com.moa.app.core.model.onboarding.Profile
import com.moa.app.core.model.onboarding.Term
import com.moa.app.core.model.onboarding.WorkPolicy
import kotlinx.collections.immutable.ImmutableList

interface OnboardingRepository {
    suspend fun getOnboardingStatus(): OnboardingStatus
    suspend fun getRandomNickName(): String
    suspend fun patchProfile(profile: Profile)
    suspend fun patchPayroll(payroll: Payroll)
    suspend fun patchWorkPolicy(workPolicy: WorkPolicy)
    suspend fun getTerms(): ImmutableList<Term>
    suspend fun putTerms(terms: ImmutableList<Term>)
}