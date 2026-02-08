package com.moa.app.data.repository

import com.moa.app.core.model.setting.SettingMenu
import com.moa.app.core.model.setting.WorkInfo

interface SettingRepository {
    suspend fun getSettingMenu(): SettingMenu
    suspend fun getWorkInfo(): WorkInfo
}