package com.moa.app.presentation.ui.setting.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.designsystem.component.MoaDialogProperties
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.navigation.OnboardingNavigation
import com.moa.app.presentation.navigation.RootNavigation
import com.moa.app.presentation.navigation.SettingNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingUiState(
    val oauthType: String = "카카오",
    val nickName: String = "집계사장",
    val latestAppVersion: String = "1.0.0",
    val dialog: MoaDialogProperties? = null,
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
            is SettingMenuIntent.SetDialog -> setDialog(intent.dialog)
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
        viewModelScope.launch {
            moaSideEffectBus.emit(
                MoaSideEffect.Navigate(
                    destination = RootNavigation.Onboarding(
                        startDestination = OnboardingNavigation.Nickname(
                            args = OnboardingNavigation.Nickname.NicknameNavigationArgs(
                                nickName = uiState.value.nickName,
                                isOnboarding = false,
                            )
                        )
                    )
                )
            )
        }
    }

    private fun workInfo() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(SettingNavigation.WorkInfo))
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

    private fun setDialog(dialog: MoaDialogProperties?) {
        _uiState.value = uiState.value.copy(
            dialog = dialog
        )
    }

    private fun logout() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(RootNavigation.Onboarding()))
        }
    }

    private fun withdraw() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(SettingNavigation.WithDraw))
        }
    }
}