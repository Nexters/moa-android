package com.moa.app.data.repository

import com.moa.app.core.model.setting.NotificationId
import com.moa.app.core.model.setting.NotificationSetting
import com.moa.app.core.model.setting.SettingMenu
import com.moa.app.core.model.setting.WithdrawalReason
import com.moa.app.core.model.setting.WorkInfo
import kotlinx.collections.immutable.ImmutableList

interface SettingRepository {
    suspend fun getSettingMenu(): SettingMenu
    suspend fun getWorkInfo(): WorkInfo
    suspend fun putSalaryDay(day: Int)
    suspend fun putCompanyName(companyName: String)
    suspend fun getNotificationSettings(): ImmutableList<NotificationSetting>
    suspend fun putNotificationSetting(id: NotificationId, enabled: Boolean)
    suspend fun withDraw(reasons: ImmutableList<WithdrawalReason>)
}