package com.moa.app.data.remote.api

import com.moa.app.data.remote.model.ApiResponse
import com.moa.app.data.remote.model.StatusResponse
import retrofit2.http.GET

interface OnboardingService {
    @GET("/api/v1/onboarding/status")
    suspend fun getStatus(): ApiResponse<StatusResponse>
}