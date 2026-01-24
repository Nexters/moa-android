package com.moa.app.presentation.ui.onboarding.nickname

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.navigation.OnboardingNavigation
import com.moa.app.presentation.ui.onboarding.OnboardingNavigationArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NickNameViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
) : ViewModel() {
    val nickNameTextFieldState = TextFieldState()

    fun onIntent(intent: NickNameIntent) {
        when (intent) {
            is NickNameIntent.ClickBack -> back()
            is NickNameIntent.ClickRandom -> random()
            is NickNameIntent.ClickNext -> next()
        }
    }

    private fun back() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(OnboardingNavigation.Back))
        }
    }

    private fun random() {

    }

    private fun next() {
        viewModelScope.launch {
            moaSideEffectBus.emit(
                sideEffect = MoaSideEffect.Navigate(
                    destination = OnboardingNavigation.WorkPlace(
                        args = OnboardingNavigationArgs().copy(nickName = nickNameTextFieldState.text.toString())
                    )
                )
            )
        }
    }
}