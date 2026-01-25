package com.moa.app.presentation.ui.setting.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.model.SalaryType
import com.moa.app.presentation.model.SettingInfo
import com.moa.app.presentation.model.UserInfo
import com.moa.app.presentation.model.WorkScheduleDay
import com.moa.app.presentation.navigation.SettingNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingUiState(
    val settingInfo: SettingInfo = SettingInfo(
        userInfo = UserInfo(
            oauthType = "카카오",
            nickName = "집계사장",
            salaryType = SalaryType.Monthly,
            salary = "300만원",
            salaryDate = 25,
            workPlace = "집계리아",
            workScheduleDays = persistentSetOf(
                WorkScheduleDay.MONDAY,
                WorkScheduleDay.TUESDAY,
                WorkScheduleDay.WEDNESDAY,
                WorkScheduleDay.THURSDAY,
                WorkScheduleDay.FRIDAY,
            ),
            workStartTime = "09:00",
            workEndTime = "18:00",
            lunchStartTime = "12:00",
            lunchEndTime = "13:00",
        ),
        latestAppVersion = "1.0.0",
    ),
)

@HiltViewModel
class SettingMenuViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingUiState())
    val uiState = _uiState.asStateFlow()

    fun onIntent(intent: SettingMenuIntent) {
        when (intent) {
            SettingMenuIntent.ClickBack -> back()
            SettingMenuIntent.ClickNickName -> nickName()
            SettingMenuIntent.ClickWorkInfo -> workInfo()
            SettingMenuIntent.ClickNotificationSetting -> notificationSetting()
            SettingMenuIntent.ClickTerms -> terms()
            SettingMenuIntent.ClickLogout -> logout()
            SettingMenuIntent.ClickWithdraw -> withdraw()
        }
    }

    private fun back() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(SettingNavigation.Back))
        }
    }

    private fun nickName() {

    }

    private fun workInfo() {
        viewModelScope.launch {
            moaSideEffectBus.emit(
                MoaSideEffect.Navigate(
                    destination = SettingNavigation.WorkInfo(
                        args = SettingNavigation.WorkInfo.WorkInfoArgs(
                            oauthType = _uiState.value.settingInfo.userInfo.oauthType,
                            salaryType = _uiState.value.settingInfo.userInfo.salaryType,
                            salary = _uiState.value.settingInfo.userInfo.salary,
                            salaryDate = _uiState.value.settingInfo.userInfo.salaryDate,
                            workPlace = _uiState.value.settingInfo.userInfo.workPlace,
                            workScheduleDays = _uiState.value.settingInfo.userInfo.workScheduleDays,
                            workStartTime = _uiState.value.settingInfo.userInfo.workStartTime,
                            workEndTime = _uiState.value.settingInfo.userInfo.workEndTime,
                            lunchStartTime = _uiState.value.settingInfo.userInfo.lunchStartTime,
                            lunchEndTime = _uiState.value.settingInfo.userInfo.lunchEndTime,
                        )
                    )
                )
            )
        }
    }

    private fun notificationSetting() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(SettingNavigation.NotificationSetting))
        }
    }

    private fun terms() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(SettingNavigation.Terms))
        }
    }

    private fun logout() {

    }

    private fun withdraw() {

    }
}