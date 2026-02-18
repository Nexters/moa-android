package com.moa.app.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.model.RootNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
) : ViewModel() {
    val moaSideEffects = moaSideEffectBus.sideEffects

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.NavigateToHistory -> navigateToHistory()
            is HomeIntent.NavigateToSetting -> navigateToSetting()
        }
    }

    private fun navigateToHistory() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(RootNavigation.History))
        }
    }

    private fun navigateToSetting() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(RootNavigation.Setting))
        }
    }
}