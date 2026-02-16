package com.moa.app.data.remote.api

import com.moa.app.data.remote.model.response.ApiResponse
import com.moa.app.data.remote.model.response.StatusResponse
import retrofit2.http.GET

interface OnboardingService {
    @GET("/api/v1/onboarding/status")
    suspend fun getStatus(): ApiResponse<StatusResponse>
}