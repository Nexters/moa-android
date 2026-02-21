package com.moa.app.data.remote.api

import com.moa.app.data.remote.model.response.MemberResponse
import com.moa.app.data.remote.model.response.PayrollResponse
import com.moa.app.data.remote.model.response.ProfileResponse
import com.moa.app.data.remote.model.response.VersionResponse
import com.moa.app.data.remote.model.response.WorkPolicyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MoaService {
    @GET("/api/v1/version")
    suspend fun getVersion(@Query("osType") osType: String = "ANDROID"): VersionResponse

    @GET("/api/v1/member")
    suspend fun getMember(): MemberResponse

    @GET("/api/v1/profile")
    suspend fun getProfile(): ProfileResponse

    @GET("/api/v1/payroll")
    suspend fun getPayroll(): PayrollResponse

    @GET("/api/v1/work-policy")
    suspend fun getWorkPolicy(): WorkPolicyResponse
}