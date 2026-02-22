package com.moa.salary.app.data.repository

import com.moa.salary.app.core.model.onboarding.OnboardingStatus
import com.moa.salary.app.core.model.onboarding.Payroll
import com.moa.salary.app.core.model.onboarding.Term
import com.moa.salary.app.core.model.onboarding.WorkPolicy
import kotlinx.collections.immutable.ImmutableList

interface OnboardingRepository {
    suspend fun getOnboardingStatus(): OnboardingStatus
    suspend fun patchNickname(nickName: String)
    suspend fun patchPayroll(payroll: Payroll)
    suspend fun getTerms(): ImmutableList<Term>
    suspend fun patchWorkPolicy(workPolicy: WorkPolicy)
    suspend fun putTerms(terms: ImmutableList<Term>)
}