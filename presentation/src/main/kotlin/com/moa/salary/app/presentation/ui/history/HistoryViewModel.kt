package com.moa.salary.app.presentation.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.salary.app.presentation.bus.MoaSideEffectBus
import com.moa.salary.app.presentation.model.MoaSideEffect
import com.moa.salary.app.presentation.model.RootNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
) : ViewModel() {
    val moaSideEffects = moaSideEffectBus.sideEffects

    fun onIntent(intent: HistoryIntent) {
        when (intent) {
            is HistoryIntent.RootBack -> rootBack()
        }
    }

    private fun rootBack() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(RootNavigation.Back))
        }
    }
}