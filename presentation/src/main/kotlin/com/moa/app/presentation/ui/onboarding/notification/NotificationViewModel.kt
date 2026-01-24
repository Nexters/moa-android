package com.moa.app.presentation.ui.onboarding.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.navigation.OnboardingScreen
import com.moa.app.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus
) : ViewModel() {
    fun onIntent(intent: NotificationIntent) {
        when (intent) {
            is NotificationIntent.ClickBack -> back()
            is NotificationIntent.ClickNext -> next()
        }
    }

    private fun back() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(OnboardingScreen.Back))
        }
    }

    private fun next() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(Screen.Home))
        }
    }
}