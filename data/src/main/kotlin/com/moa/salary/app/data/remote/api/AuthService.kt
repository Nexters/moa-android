package com.moa.salary.app.data.remote.api

import com.moa.salary.app.data.remote.model.request.LogoutRequest
import com.moa.salary.app.data.remote.model.request.TokenRequest
import com.moa.salary.app.data.remote.model.request.WithDrawRequest
import com.moa.salary.app.data.remote.model.response.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/api/v1/auth/kakao")
    suspend fun postToken(@Body tokenRequest: TokenRequest): TokenResponse

    @POST("/api/v1/auth/logout")
    suspend fun logout(@Body logoutRequest: LogoutRequest)

    @POST("/api/v1/member/withdrawal")
    suspend fun withdraw(@Body withDrawRequest: WithDrawRequest)
}