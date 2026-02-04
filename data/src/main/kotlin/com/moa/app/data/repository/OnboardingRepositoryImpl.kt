package com.moa.app.data.repository

import com.moa.app.core.model.OnboardingState
import javax.inject.Inject
import kotlin.random.Random

class OnboardingRepositoryImpl @Inject constructor(

) : OnboardingRepository {
    override suspend fun fetchOnboardingState(): OnboardingState {
        return OnboardingState(
            loginCompleted = false,
            nickNameCompleted = false,
            workPlaceCompleted = false,
            salaryCompleted = false,
            workScheduleCompleted = false,
        )
    }

    override suspend fun fetchRandomNickName(): String {
        return "집계사장${Random.nextInt(10)}"
    }

    override suspend fun putNickName(nickName: String) {}

    override suspend fun putWorkPlace(workPlace: String) {}
}