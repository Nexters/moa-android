package com.moa.salary.app.data.repository

import com.moa.salary.app.core.model.onboarding.Payroll
import com.moa.salary.app.core.model.onboarding.WorkPolicy
import com.moa.salary.app.core.model.setting.NotificationSetting
import com.moa.salary.app.core.model.setting.SettingMenu
import com.moa.salary.app.core.model.setting.SettingTerm
import com.moa.salary.app.core.model.setting.WorkInfo
import kotlinx.collections.immutable.ImmutableList

interface SettingRepository {
    suspend fun getSettingMenu(): SettingMenu
    suspend fun getWorkInfo(): WorkInfo
    suspend fun getTerms(): ImmutableList<SettingTerm>
    suspend fun patchNickname(nickname: String)
    suspend fun patchCompanyName(companyName: String)
    suspend fun patchPaydayDay(paydayDay: Int)
    suspend fun patchPayroll(payroll: Payroll)
    suspend fun patchWorkPolicy(workPolicy: WorkPolicy)
    suspend fun getNotificationSettings(): ImmutableList<NotificationSetting>
    suspend fun patchNotificationSetting(notificationSetting: NotificationSetting.Content)
}