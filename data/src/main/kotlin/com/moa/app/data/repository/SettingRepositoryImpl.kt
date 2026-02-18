package com.moa.app.data.repository

import com.moa.app.core.model.setting.NotificationId
import com.moa.app.core.model.setting.NotificationSetting
import com.moa.app.core.model.setting.OAuthType
import com.moa.app.core.model.setting.SettingMenu
import com.moa.app.core.model.setting.WithdrawalReason
import com.moa.app.core.model.setting.WorkInfo
import com.moa.app.data.remote.api.MoaService
import com.moa.app.data.remote.api.SettingService
import com.moa.app.data.remote.mapper.toDomain
import com.moa.app.data.remote.model.request.NicknameRequest
import com.moa.app.data.remote.model.request.PaydayDayRequest
import com.moa.app.data.remote.model.request.WorkplaceRequest
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(
    private val moaService: MoaService,
    private val settingService: SettingService,
) : SettingRepository {
    override suspend fun getSettingMenu(): SettingMenu {
        return coroutineScope {
            val memberDeferred = async { moaService.getMember() }
            val profileDeferred = async { moaService.getProfile() }
            // TODO version api
            val versionDeferred = "1.0.0"

            SettingMenu(
                oAuthType = OAuthType.valueOf(memberDeferred.await().provider),
                nickName = profileDeferred.await().nickname,
                latestAppVersion = versionDeferred
            )
        }
    }

    override suspend fun getWorkInfo(): WorkInfo {
        return coroutineScope {
            val memberDeferred = async { moaService.getMember() }
            val profileDeferred = async { moaService.getProfile() }
            val payrollDeferred = async { moaService.getPayroll() }
            val workPolicyDeferred = async { moaService.getWorkPolicy() }

            val member = memberDeferred.await()
            val profile = profileDeferred.await().toDomain()
            val payroll = payrollDeferred.await().toDomain()
            val workPolicy = workPolicyDeferred.await().toDomain()

            WorkInfo(
                oAuthType = OAuthType.valueOf(member.provider),
                payroll = payroll,
                paydayDay = profile.paydayDay,
                companyName = profile.companyName,
                workPolicy = workPolicy,
            )
        }
    }

    override suspend fun patchNickname(nickname: String) {
        settingService.patchNickname(NicknameRequest(nickname))
    }

    override suspend fun patchCompanyName(companyName: String) {
        settingService.patchWorkplace(WorkplaceRequest(companyName))
    }

    override suspend fun patchPaydayDay(paydayDay: Int) {
        settingService.patchPaydayDay(PaydayDayRequest(paydayDay))
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