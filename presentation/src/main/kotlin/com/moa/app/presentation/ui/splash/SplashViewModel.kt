package com.moa.app.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.navigation.RootNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
) : ViewModel() {
    fun checkLoginStatus() {
        viewModelScope.launch {
            // TODO : Implement actual login status check
            val loginStatus = true
            delay(3000)

            if (loginStatus) {
                moaSideEffectBus.emit(MoaSideEffect.Navigate(RootNavigation.Onboarding()))
            } else {
                moaSideEffectBus.emit(MoaSideEffect.Navigate(RootNavigation.Home))
            }
        }
    }
}