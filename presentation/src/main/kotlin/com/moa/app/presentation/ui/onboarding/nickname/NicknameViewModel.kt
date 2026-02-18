package com.moa.app.presentation.ui.onboarding.nickname

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.data.repository.OnboardingRepository
import com.moa.app.data.repository.SettingRepository
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.extensions.execute
import com.moa.app.presentation.manager.RandomNickManager
import com.moa.app.presentation.model.MoaDialogProperties
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.model.OnboardingNavigation
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = NicknameViewModel.Factory::class)
class NicknameViewModel @AssistedInject constructor(
    @Assisted private val args: OnboardingNavigation.Nickname.NicknameNavigationArgs,
    private val moaSideEffectBus: MoaSideEffectBus,
    private val onboardingRepository: OnboardingRepository,
    private val settingRepository: SettingRepository,
) : ViewModel() {
    val nicknameTextFieldState = TextFieldState(args.nickname)

    init {
        if (args.isOnboarding) {
            random()
        }
    }

    fun onIntent(intent: NicknameIntent) {
        when (intent) {
            is NicknameIntent.ClickBack -> back()
            is NicknameIntent.ClickRandom -> random()
            is NicknameIntent.ClickNext -> next()
        }
    }

    private fun back() {
        viewModelScope.launch {
            if (args.isOnboarding) {
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
        val nickName = RandomNickManager.get()
        nicknameTextFieldState.setTextAndPlaceCursorAtEnd(nickName)
    }

    private fun next() {
        if (args.isOnboarding) {
            nextIfIsOnboarding()
        } else {
            nextIfIsNotOnboarding()
        }
    }

    private fun nextIfIsOnboarding() {
        suspend {
            onboardingRepository.patchNickname(nicknameTextFieldState.text.toString())
        }.execute(
            bus = moaSideEffectBus,
            scope = viewModelScope,
            onRetry = { nextIfIsOnboarding() }
        ) {
            viewModelScope.launch {
                moaSideEffectBus.emit(MoaSideEffect.Navigate(OnboardingNavigation.Salary()))
            }
        }
    }

    private fun nextIfIsNotOnboarding() {
        suspend {
            settingRepository.patchNickname(nicknameTextFieldState.text.toString())
        }.execute(
            bus = moaSideEffectBus,
            scope = viewModelScope,
            onRetry = { nextIfIsNotOnboarding() }
        ) {
            viewModelScope.launch {
                moaSideEffectBus.emit(MoaSideEffect.Navigate(OnboardingNavigation.Back))
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(args: OnboardingNavigation.Nickname.NicknameNavigationArgs): NicknameViewModel
    }
}