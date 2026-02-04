package com.moa.app.data.repository

import com.moa.app.core.model.OnboardingState
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(

) : OnboardingRepository {
    override suspend fun fetchOnboardingState(): OnboardingState {
        return OnboardingState(
            loginCompleted = true,
            nickNameCompleted = true,
            workPlaceCompleted = true,
            salaryCompleted = false,
            workScheduleCompleted = false,
        )
    }

    override suspend fun putNickName(nickName: String) {}

    override suspend fun putWorkPlace(workPlace: String) {}
}