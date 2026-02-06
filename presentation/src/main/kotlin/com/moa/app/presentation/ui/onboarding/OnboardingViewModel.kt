package com.moa.app.presentation.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.model.RootNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
) : ViewModel() {
    val moaSideEffects = moaSideEffectBus.sideEffects

    fun onIntent(intent: OnboardingIntent) {
        when (intent) {
            is OnboardingIntent.RootBack -> rootBack()
        }
    }

    private fun rootBack() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(RootNavigation.Back))
        }
    }
}