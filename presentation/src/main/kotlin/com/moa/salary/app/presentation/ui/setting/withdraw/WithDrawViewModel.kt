package com.moa.salary.app.presentation.ui.setting.withdraw

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.salary.app.core.model.setting.WithdrawalReason
import com.moa.salary.app.data.repository.AuthRepository
import com.moa.salary.app.data.repository.TokenRepository
import com.moa.salary.app.presentation.bus.MoaSideEffectBus
import com.moa.salary.app.presentation.extensions.execute
import com.moa.salary.app.presentation.model.MoaSideEffect
import com.moa.salary.app.presentation.model.OnboardingNavigation
import com.moa.salary.app.presentation.model.RootNavigation
import com.moa.salary.app.presentation.model.SettingNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WithDrawViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
    private val tokenRepository: TokenRepository,
    private val authRepository: AuthRepository,
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
            authRepository.withdraw(selectedReasons)
            tokenRepository.clearToken()
        }.execute(
            bus = moaSideEffectBus,
            scope = viewModelScope,
            onRetry = { withDraw() }
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