package com.moa.salary.app.data.repository

import com.moa.salary.app.core.model.onboarding.Token
import com.moa.salary.app.core.model.setting.WithdrawalReason
import kotlinx.collections.immutable.ImmutableList

interface AuthRepository {
    suspend fun postToken(
        idToken: String,
        fcmDeviceToken: String,
    ): Token

    suspend fun logout(fcmDeviceToken: String)

    suspend fun withdraw(reasons: ImmutableList<WithdrawalReason>)

    suspend fun updateToken(token: String)
}