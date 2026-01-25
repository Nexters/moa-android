package com.moa.app.presentation.model

import kotlinx.collections.immutable.ImmutableSet

data class SettingInfo(
    val userInfo: UserInfo,
    val latestAppVersion: String,
)

data class UserInfo(
    val oauthType: String,
    val nickName: String,
    val salaryType: SalaryType,
    val salary: String,
    val salaryDate: Int,
    val workPlace: String,
    val workScheduleDays: ImmutableSet<WorkScheduleDay>,
    val workStartTime: String,
    val workEndTime: String,
    val lunchStartTime: String,
    val lunchEndTime: String,
)
