package com.moa.app.presentation.ui.setting.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.navigation.SettingNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingMenuViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
) : ViewModel() {

    fun onIntent(intent : SettingMenuIntent) {
        when(intent) {
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

    private fun nickName () {

    }

    private fun workInfo() {

    }

    private fun notificationSetting() {

    }

    private fun terms() {

    }

    private fun logout() {

    }

    private fun withdraw() {

    }
}