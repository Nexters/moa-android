package com.moa.app.data.remote.api

import com.moa.app.data.remote.model.ApiResponse
import com.moa.app.data.remote.model.TokenRequest
import com.moa.app.data.remote.model.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenService {
    @POST("/api/v1/auth/kakao")
    suspend fun postToken(@Body tokenRequest: TokenRequest): ApiResponse<TokenResponse>
}