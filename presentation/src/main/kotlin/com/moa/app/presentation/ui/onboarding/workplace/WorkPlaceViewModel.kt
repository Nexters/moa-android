package com.moa.app.presentation.ui.onboarding.workplace

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.navigation.OnboardingScreen
import com.moa.app.presentation.ui.onboarding.OnboardingNavigationArgs
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = WorkPlaceViewModel.Factory::class)
class WorkPlaceViewModel @AssistedInject constructor(
    @Assisted private val args: OnboardingNavigationArgs,
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
            moaSideEffectBus.emit(
                sideEffect = MoaSideEffect.Navigate(
                    destination = OnboardingScreen.Salary(
                        args = args.copy(workPlace = workPlaceTextFieldState.text.toString())
                    )
                )
            )
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(args: OnboardingNavigationArgs): WorkPlaceViewModel
    }
}