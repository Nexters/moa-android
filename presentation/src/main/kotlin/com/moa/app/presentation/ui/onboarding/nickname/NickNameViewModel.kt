package com.moa.app.presentation.ui.onboarding.nickname

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.designsystem.component.MoaDialogProperties
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.navigation.OnboardingNavigation
import com.moa.app.presentation.navigation.RootNavigation
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
            if (nickNameTextFieldState.text.isNotBlank()) {
                moaSideEffectBus.emit(
                    MoaSideEffect.Dialog(
                        MoaDialogProperties.Confirm(
                            title = "정말 그만 작성하실 건가요?",
                            message = "뒤로 돌아가기를 누르면\n" +
                                    "지금까지 작성한 정보가 사라져요",
                            positiveText = "아니오",
                            negativeText = "네",
                            onPositive = {},
                            onNegative = {
                                moaSideEffectBus.emit(MoaSideEffect.Navigate(OnboardingNavigation.Back))
                            },
                        )
                    )
                )
            } else {
                moaSideEffectBus.emit(MoaSideEffect.Navigate(OnboardingNavigation.Back))
            }
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
                            OnboardingNavigation.WorkPlace.WorkPlaceNavigationArgs()
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