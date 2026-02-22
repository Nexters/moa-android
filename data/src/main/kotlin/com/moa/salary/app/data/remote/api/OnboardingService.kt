package com.moa.salary.app.data.remote.api

import com.moa.salary.app.data.remote.model.request.AgreementsRequest
import com.moa.salary.app.data.remote.model.request.PayrollRequest
import com.moa.salary.app.data.remote.model.request.NicknameRequest
import com.moa.salary.app.data.remote.model.request.WorkPolicyRequest
import com.moa.salary.app.data.remote.model.response.AgreementsResponse
import com.moa.salary.app.data.remote.model.response.PayrollResponse
import com.moa.salary.app.data.remote.model.response.ProfileResponse
import com.moa.salary.app.data.remote.model.response.StatusResponse
import com.moa.salary.app.data.remote.model.response.TermsResponse
import com.moa.salary.app.data.remote.model.response.WorkPolicyResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT

interface OnboardingService {
    @GET("/api/v1/onboarding/status")
    suspend fun getStatus(): StatusResponse

    @PATCH("/api/v1/onboarding/profile")
    suspend fun patchProfile(@Body nicknameRequest: NicknameRequest): ProfileResponse

    @PATCH("/api/v1/onboarding/payroll")
    suspend fun patchPayroll(@Body payrollRequest: PayrollRequest): PayrollResponse

    @GET("/api/v1/onboarding/terms")
    suspend fun getTerms(): TermsResponse

    @PATCH("/api/v1/onboarding/work-policy")
    suspend fun patchWorkPolicy(@Body workPolicyRequest: WorkPolicyRequest): WorkPolicyResponse

    @PUT("/api/v1/onboarding/terms/agreements")
    suspend fun putAgreements(@Body agreementsRequest: AgreementsRequest): AgreementsResponse
}