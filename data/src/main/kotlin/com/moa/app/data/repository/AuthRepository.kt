package com.moa.app.data.repository

import com.moa.app.core.model.setting.WithdrawalReason
import kotlinx.collections.immutable.ImmutableList

interface AuthRepository {
    suspend fun postToken(
        idToken: String,
        fcmDeviceToken: String,
    ): String

    suspend fun logout(fcmDeviceToken: String)

    suspend fun withdraw(reasons: ImmutableList<WithdrawalReason>)
}