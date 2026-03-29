package com.moa.salary.app.data.remote.api

import com.moa.salary.app.data.remote.model.response.CalendarResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CalendarService {
    @GET("api/v1/calendar")
    suspend fun getCalendar(
        @Query("year") year: Int,
        @Query("month") month: Int,
    ): CalendarResponse
}