package com.moa.app.presentation.ui.setting.withdraw

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.core.model.setting.WithdrawalReason
import com.moa.app.data.repository.SettingRepository
import com.moa.app.data.repository.TokenRepository
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.extensions.execute
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.model.OnboardingNavigation
import com.moa.app.presentation.model.RootNavigation
import com.moa.app.presentation.model.SettingNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WithDrawViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
    private val settingRepository: SettingRepository,
    private val tokenRepository: TokenRepository,
) : ViewModel() {

    var selectedReasons by mutableStateOf<ImmutableList<WithdrawalReason>>(persistentListOf())

    fun onIntent(intent: WithDrawIntent) {
        when (intent) {
            WithDrawIntent.ClickBack -> back()
            is WithDrawIntent.ClickReason -> reason(intent.reason)
            WithDrawIntent.ClickWithDraw -> withDraw()
        }
    }

    private fun back() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(SettingNavigation.Back))
        }
    }

    private fun reason(reason: WithdrawalReason) {
        val updateReasons = selectedReasons.toMutableList()

        if (selectedReasons.contains(reason)) {
            updateReasons.remove(reason)
        } else {
            updateReasons.add(reason)
        }

        selectedReasons = updateReasons.toImmutableList()
    }

    private fun withDraw() {
        suspend {
            settingRepository.withDraw(selectedReasons)
        }.execute(
            bus = moaSideEffectBus,
            scope = viewModelScope,
        ) {
            clearToken()
        }
    }

    private fun clearToken() {
        suspend {
            tokenRepository.clearToken()
        }.execute(
            bus = moaSideEffectBus,
            scope = viewModelScope,
        ) {
            viewModelScope.launch {
                moaSideEffectBus.emit(
                    MoaSideEffect.Navigate(
                        destination = RootNavigation.Onboarding(
                            startDestination = OnboardingNavigation.Login
                        )
                    )
                )
            }
        }
    }
}