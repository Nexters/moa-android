package com.moa.app.presentation.ui.setting.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.core.model.setting.SettingMenu
import com.moa.app.data.repository.SettingRepository
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.extensions.execute
import com.moa.app.presentation.model.MoaDialogProperties
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.model.OnboardingNavigation
import com.moa.app.presentation.model.RootNavigation
import com.moa.app.presentation.model.SettingNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingUiState(
    val settingMenu: SettingMenu = SettingMenu(
        oAuthType = null,
        nickName = "",
        latestAppVersion = "1.0.0",
    )
)

@HiltViewModel
class SettingMenuViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
    private val settingRepository: SettingRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getSettingMenu()
    }

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

    private fun getSettingMenu() {
        suspend {
            settingRepository.getSettingMenu()
        }.execute(
            bus = moaSideEffectBus,
            scope = viewModelScope,
        ) {
            _uiState.value = _uiState.value.copy(settingMenu = it)
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
                                nickName = uiState.value.settingMenu.nickName,
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

    private fun logout() {
        viewModelScope.launch {
            moaSideEffectBus.emit(
                MoaSideEffect.Dialog(
                    MoaDialogProperties.Confirm(
                        title = "로그아웃 하시겠어요?",
                        message = "로그아웃하면 로그인 화면으로 이동해요.",
                        onPositive = {
                            moaSideEffectBus.emit(
                                MoaSideEffect.Navigate(
                                    destination = RootNavigation.Onboarding(
                                        startDestination = OnboardingNavigation.Login
                                    )
                                )
                            )
                        },
                    )
                )
            )
        }
    }

    private fun withdraw() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(SettingNavigation.WithDraw))
        }
    }
}