package com.moa.app.core.model.setting

data class SettingMenu(
    val oAuthType: OAuthType?,
    val nickName: String,
    val latestAppVersion: String,
)
