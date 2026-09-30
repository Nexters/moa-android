package com.moa.salary.app.data.remote.api

import com.moa.salary.app.data.remote.model.request.FcmRequest
import com.moa.salary.app.data.remote.model.request.LogoutRequest
import com.moa.salary.app.data.remote.model.request.RefreshTokenRequest
import com.moa.salary.app.data.remote.model.request.TokenRequest
import com.moa.salary.app.data.remote.model.request.WithDrawRequest
import com.moa.salary.app.data.remote.model.response.RefreshTokenResponse
import com.moa.salary.app.data.remote.model.response.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthService {
    @POST("/api/v1/auth/kakao")
    suspend fun postToken(@Body tokenRequest: TokenRequest): TokenResponse

    @POST(REFRESH_PATH)
    suspend fun refresh(@Body refreshTokenRequest: RefreshTokenRequest): RefreshTokenResponse

    @POST("/api/v1/auth/logout")
    suspend fun logout(@Body logoutRequest: LogoutRequest)

    @POST("/api/v1/member/withdrawal")
    suspend fun withdraw(@Body withDrawRequest: WithDrawRequest)

    @PUT("/api/v1/fcm/token")
    suspend fun updateToken(@Body fcmRequest: FcmRequest)

    companion object {
        const val REFRESH_PATH = "/api/v1/auth/refresh"
    }
}
