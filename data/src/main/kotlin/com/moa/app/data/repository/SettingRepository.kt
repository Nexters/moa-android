package com.moa.app.data.repository

import com.moa.app.core.model.setting.SettingMenu

interface SettingRepository {
    suspend fun getSettingMenu(): SettingMenu
}