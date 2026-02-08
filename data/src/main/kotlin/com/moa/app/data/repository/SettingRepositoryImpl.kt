package com.moa.app.data.repository

import com.moa.app.core.model.setting.OAuthType
import com.moa.app.core.model.setting.SettingMenu
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
}