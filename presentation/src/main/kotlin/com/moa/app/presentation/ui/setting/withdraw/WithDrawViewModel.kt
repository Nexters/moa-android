package com.moa.app.presentation.ui.setting.withdraw

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.model.WithdrawalReason
import com.moa.app.presentation.navigation.SettingNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WithDrawViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
) : ViewModel() {

    var selectedReasons by mutableStateOf<ImmutableSet<WithdrawalReason>>(persistentSetOf())

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
        val updateReasons = selectedReasons.toMutableSet()

        if (selectedReasons.contains(reason)) {
            updateReasons.remove(reason)
        } else {
            updateReasons.add(reason)
        }

        selectedReasons = updateReasons.toImmutableSet()
    }

    private fun withDraw() {
        // TODO 탈퇴 처리
    }
}