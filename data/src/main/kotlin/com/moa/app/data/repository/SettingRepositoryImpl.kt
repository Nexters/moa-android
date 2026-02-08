package com.moa.app.data.repository

import com.moa.app.core.model.onboarding.Payroll
import com.moa.app.core.model.onboarding.Time
import com.moa.app.core.model.onboarding.WorkPolicy
import com.moa.app.core.model.setting.NotificationId
import com.moa.app.core.model.setting.NotificationSetting
import com.moa.app.core.model.setting.OAuthType
import com.moa.app.core.model.setting.SettingMenu
import com.moa.app.core.model.setting.WithdrawalReason
import com.moa.app.core.model.setting.WorkInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(

) : SettingRepository {
    override suspend fun getSettingMenu(): SettingMenu {
        return SettingMenu(
            oAuthType = OAuthType.KAKAO,
            nickName = "집계사장",
            latestAppVersion = "1.0.0",
        )
    }

    override suspend fun getWorkInfo(): WorkInfo {
        return WorkInfo(
            oAuthType = OAuthType.KAKAO,
            payroll = Payroll(
                salary = "40000000",
                salaryType = Payroll.SalaryType.ANNUAL,
            ),
            companyName = "",
            workPolicy = WorkPolicy(
                workScheduleDays = persistentListOf(
                    WorkPolicy.WorkScheduleDay.MON,
                    WorkPolicy.WorkScheduleDay.TUE,
                    WorkPolicy.WorkScheduleDay.WED,
                    WorkPolicy.WorkScheduleDay.THU,
                    WorkPolicy.WorkScheduleDay.FRI,
                ),
                time = Time(
                    startHour = 9,
                    startMinute = 0,
                    endHour = 18,
                    endMinute = 0,
                )
            )
        )
    }

    override suspend fun putSalaryDay(day: Int) {
        // TODO put salaryday
    }

    override suspend fun getNotificationSettings(): ImmutableList<NotificationSetting> {
        return persistentListOf(
            NotificationSetting.Service(
                id = NotificationId.COMMUTE,
                title = "출퇴근 알림",
                enabled = true,
            ),
            NotificationSetting.Service(
                id = NotificationId.SALARY_DAY,
                title = "월급날 알림",
                enabled = true,
            ),
            NotificationSetting.Marketing(
                id = NotificationId.BENEFITS,
                title = "혜택 및 이벤트 알림",
                enabled = true,
            ),
        )
    }

    override suspend fun putNotificationSetting(id: NotificationId, enabled: Boolean) {
        // TODO put notificationsetting
    }

    override suspend fun withDraw(reasons: ImmutableList<WithdrawalReason>) {
        // TODO withdraw
    }
}