package com.moa.app.data.remote.api

import com.moa.app.data.remote.model.request.ProfileRequest
import com.moa.app.data.remote.model.response.ApiResponse
import com.moa.app.data.remote.model.response.ProfileResponse
import com.moa.app.data.remote.model.response.StatusResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface OnboardingService {
    @GET("/api/v1/onboarding/status")
    suspend fun getStatus(): ApiResponse<StatusResponse>

    @PATCH("/api/v1/onboarding/profile")
    suspend fun patchProfile(@Body profileRequest: ProfileRequest): ApiResponse<ProfileResponse>
}