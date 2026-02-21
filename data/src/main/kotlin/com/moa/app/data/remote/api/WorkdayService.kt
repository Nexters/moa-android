package com.moa.app.data.remote.api

import com.moa.app.data.remote.model.request.UpdateWorkdayRequest
import com.moa.app.data.remote.model.response.WorkdayItem
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface WorkdayService {
    @GET("/api/v1/workdays")
    suspend fun getWorkdays(
        @Query("year") year: Int,
        @Query("month") month: Int,
    ): List<WorkdayItem>

    @GET("/api/v1/workdays/{date}")
    suspend fun getWorkday(@Path("date") date: String):

    @PUT("/api/v1/workdays/{date}")
    suspend fun putWorkday(
        @Path("date") date: String,
        @Body request: UpdateWorkdayRequest,
    )
}