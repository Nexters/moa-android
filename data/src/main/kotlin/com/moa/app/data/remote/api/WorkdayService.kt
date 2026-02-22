package com.moa.app.data.remote.api

import com.moa.app.data.remote.dto.request.ClockOutRequest
import com.moa.app.data.remote.dto.request.WorkdayRequest
import com.moa.app.data.remote.dto.response.EarningsResponse
import com.moa.app.data.remote.dto.response.WorkdayContent
import com.moa.app.data.remote.dto.response.WorkdayDetailResponse
import com.moa.app.data.remote.dto.response.WorkdayListItem
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface WorkdayService {
    @PUT("/api/v1/workdays/{date}")
    suspend fun updateWorkday(
        @Path("date") date: String,
        @Body request: WorkdayRequest,
    ): WorkdayContent

    @PATCH("/api/v1/workdays/{date}")
    suspend fun patchClockOut(
        @Path("date") date: String,
        @Body request: ClockOutRequest,
    ): WorkdayContent

    @GET("/api/v1/workdays")
    suspend fun getWorkdays(
        @Query("year") year: Int,
        @Query("month") month: Int,
    ): List<WorkdayListItem>

    @GET("/api/v1/workdays/{date}")
    suspend fun getWorkdayDetail(
        @Path("date") date: String,
    ): WorkdayDetailResponse

    @GET("/api/v1/workdays/earnings")
    suspend fun getEarnings(
        @Query("year") year: Int,
        @Query("month") month: Int,
    ): EarningsResponse
}