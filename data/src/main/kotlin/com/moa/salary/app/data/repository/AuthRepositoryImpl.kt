package com.moa.salary.app.data.repository

import com.moa.salary.app.core.model.setting.WithdrawalReason
import com.moa.salary.app.data.local.PreferencesDataStore
import com.moa.salary.app.data.remote.api.AuthService
import com.moa.salary.app.data.remote.model.request.LogoutRequest
import com.moa.salary.app.data.remote.model.request.TokenRequest
import com.moa.salary.app.data.remote.model.request.WithDrawRequest
import kotlinx.collections.immutable.ImmutableList
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val preferencesDataStore: PreferencesDataStore,
) : AuthRepository {
    override suspend fun postToken(
        idToken: String,
        fcmDeviceToken: String
    ): String {
        return authService.postToken(
            TokenRequest(
                idToken = idToken,
                fcmDeviceToken = fcmDeviceToken,
            )
        ).accessToken
    }

    override suspend fun logout(fcmDeviceToken: String) {
        authService.logout(LogoutRequest(fcmDeviceToken))
    }

    override suspend fun withdraw(reasons: ImmutableList<WithdrawalReason>) {
        authService.withdraw(WithDrawRequest(reasons.map { it.title }))
        preferencesDataStore.clear()
    }
}