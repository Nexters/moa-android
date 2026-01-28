package com.moa.app.presentation.ui.onboarding.nickname

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.navigation.OnboardingNavigation
import com.moa.app.presentation.navigation.RootNavigation
import com.moa.app.presentation.ui.onboarding.OnboardingNavigationArgs
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = NickNameViewModel.Factory::class)
class NickNameViewModel @AssistedInject constructor(
    @Assisted private val args: OnboardingNavigation.Nickname.NicknameNavigationArgs,
    private val moaSideEffectBus: MoaSideEffectBus,
) : ViewModel() {
    val nickNameTextFieldState = TextFieldState(args.nickName)

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
                    destination = if (args.isOnboarding) {
                        OnboardingNavigation.WorkPlace(
                            args = OnboardingNavigationArgs().copy(nickName = nickNameTextFieldState.text.toString())
                        )
                    } else {
                        RootNavigation.Back
                    }
                )
            )
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(args: OnboardingNavigation.Nickname.NicknameNavigationArgs): NickNameViewModel
    }
}