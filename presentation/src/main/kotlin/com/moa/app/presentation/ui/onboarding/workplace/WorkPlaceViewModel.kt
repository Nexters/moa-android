package com.moa.app.presentation.ui.onboarding.workplace

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.navigation.OnboardingScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkPlaceViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
) : ViewModel() {
    val workPlaceTextFieldState = TextFieldState()

    fun onIntent(intent: WorkPlaceIntent) {
        when (intent) {
            is WorkPlaceIntent.ClickBack -> back()
            is WorkPlaceIntent.ClickNext -> next()
        }
    }

    private fun back() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(OnboardingScreen.Back))
        }
    }

    private fun next() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(OnboardingScreen.Salary))
        }
    }
}