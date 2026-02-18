package com.moa.app.data.remote.api

import com.moa.app.data.remote.model.request.NicknameRequest
import com.moa.app.data.remote.model.request.NotificationSettingRequest
import com.moa.app.data.remote.model.request.PaydayDayRequest
import com.moa.app.data.remote.model.request.PayrollRequest
import com.moa.app.data.remote.model.request.WorkPolicyRequest
import com.moa.app.data.remote.model.request.WorkplaceRequest
import com.moa.app.data.remote.model.response.NotificationSettingResponse
import com.moa.app.data.remote.model.response.PayrollResponse
import com.moa.app.data.remote.model.response.ProfileResponse
import com.moa.app.data.remote.model.response.WorkPolicyResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface SettingService {
    @PATCH("/api/v1/profile/nickname")
    suspend fun patchNickname(@Body nicknameRequest: NicknameRequest): ProfileResponse

    @PATCH("/api/v1/profile/workplace")
    suspend fun patchWorkplace(@Body workplaceRequest: WorkplaceRequest): ProfileResponse

    @PATCH("/api/v1/profile/payday")
    suspend fun patchPaydayDay(@Body paydayDayRequest: PaydayDayRequest): ProfileResponse

    @PATCH("/api/v1/payroll")
    suspend fun patchPayroll(@Body payrollRequest: PayrollRequest): PayrollResponse

    @PATCH("/api/v1/work-policy")
    suspend fun patchWorkPolicy(@Body workPolicyRequest: WorkPolicyRequest): WorkPolicyResponse

    @GET("/api/v1/settings/notification")
    suspend fun getNotificationSettings(): List<NotificationSettingResponse>

    @PATCH("/api/v1/settings/notification")
    suspend fun patchNotificationSetting(@Body notificationSettingRequest: NotificationSettingRequest): List<NotificationSettingResponse>
}