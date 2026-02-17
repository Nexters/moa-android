package com.moa.app.data.remote.api

import com.moa.app.data.remote.model.request.PayrollRequest
import com.moa.app.data.remote.model.request.ProfileRequest
import com.moa.app.data.remote.model.request.WorkPolicyRequest
import com.moa.app.data.remote.model.response.ApiResponse
import com.moa.app.data.remote.model.response.PayrollResponse
import com.moa.app.data.remote.model.response.ProfileResponse
import com.moa.app.data.remote.model.response.StatusResponse
import com.moa.app.data.remote.model.response.TermsResponse
import com.moa.app.data.remote.model.response.WorkPolicyResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface OnboardingService {
    @GET("/api/v1/onboarding/status")
    suspend fun getStatus(): ApiResponse<StatusResponse>

    @PATCH("/api/v1/onboarding/profile")
    suspend fun patchProfile(@Body profileRequest: ProfileRequest): ApiResponse<ProfileResponse>

    @PATCH("/api/v1/onboarding/payroll")
    suspend fun patchPayroll(@Body payrollRequest: PayrollRequest): ApiResponse<PayrollResponse>

    @GET("/api/v1/onboarding/terms")
    suspend fun getTerms(): ApiResponse<TermsResponse>

    @PATCH("/api/v1/onboarding/work-policy")
    suspend fun patchWorkPolicy(@Body workPolicyRequest: WorkPolicyRequest): ApiResponse<WorkPolicyResponse>
}