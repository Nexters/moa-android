package com.moa.app.data.repository

import com.moa.app.core.model.onboarding.Payroll
import com.moa.app.core.model.onboarding.WorkPolicy
import com.moa.app.core.model.setting.NotificationSetting
import com.moa.app.core.model.setting.OAuthType
import com.moa.app.core.model.setting.SettingMenu
import com.moa.app.core.model.setting.SettingTerm
import com.moa.app.core.model.setting.WorkInfo
import com.moa.app.data.remote.api.MoaService
import com.moa.app.data.remote.api.SettingService
import com.moa.app.data.remote.mapper.toData
import com.moa.app.data.remote.mapper.toDomain
import com.moa.app.data.remote.mapper.toNotificationSettingDomain
import com.moa.app.data.remote.mapper.toSettingTermDomain
import com.moa.app.data.remote.model.request.NicknameRequest
import com.moa.app.data.remote.model.request.PaydayDayRequest
import com.moa.app.data.remote.model.request.WorkplaceRequest
import kotlinx.collections.immutable.ImmutableList
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
            val versionDeferred = async { moaService.getVersion() }

            SettingMenu(
                oAuthType = OAuthType.valueOf(memberDeferred.await().provider),
                nickName = profileDeferred.await().nickname,
                latestVersion = versionDeferred.await().latestVersion,
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

    override suspend fun getTerms(): ImmutableList<SettingTerm> {
        return settingService.getTerms().terms.toSettingTermDomain()
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

    override suspend fun patchPayroll(payroll: Payroll) {
        settingService.patchPayroll(payroll.toData())
    }

    override suspend fun patchWorkPolicy(workPolicy: WorkPolicy) {
        settingService.patchWorkPolicy(workPolicy.toData())
    }

    override suspend fun getNotificationSettings(): ImmutableList<NotificationSetting> {
        return settingService.getNotificationSettings().toNotificationSettingDomain()
    }

    override suspend fun patchNotificationSetting(notificationSetting: NotificationSetting.Content) {
        settingService.patchNotificationSetting(notificationSetting.toData())
    }
}