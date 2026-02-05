package com.moa.app.presentation.ui.onboarding.workplace

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.core.model.onboarding.Profile
import com.moa.app.data.repository.OnboardingRepository
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.extensions.execute
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.navigation.OnboardingNavigation
import com.moa.app.presentation.navigation.RootNavigation
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = WorkPlaceViewModel.Factory::class)
class WorkPlaceViewModel @AssistedInject constructor(
    @Assisted private val args: OnboardingNavigation.WorkPlace.WorkPlaceNavigationArgs,
    private val moaSideEffectBus: MoaSideEffectBus,
    private val onboardingRepository: OnboardingRepository,
) : ViewModel() {
    val workPlaceTextFieldState = TextFieldState(args.workPlace)

    fun onIntent(intent: WorkPlaceIntent) {
        when (intent) {
            is WorkPlaceIntent.ClickBack -> back()
            is WorkPlaceIntent.ClickNext -> next()
        }
    }

    private fun back() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(OnboardingNavigation.Back))
        }
    }

    private fun next() {
        if(args.isOnboarding){
            suspend {
                onboardingRepository.patchProfile(
                    Profile(
                        nickName = args.nickName,
                        workPlace = workPlaceTextFieldState.text.toString()
                    )
                )
            }.execute(
                bus = moaSideEffectBus,
                scope = viewModelScope,
            ) {
                viewModelScope.launch {
                    moaSideEffectBus.emit(MoaSideEffect.Navigate(OnboardingNavigation.Salary()))
                }
            }
        }else {
            // TODO setting 근무지 api
            viewModelScope.launch {
                moaSideEffectBus.emit(MoaSideEffect.Navigate(OnboardingNavigation.Back))
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(args: OnboardingNavigation.WorkPlace.WorkPlaceNavigationArgs): WorkPlaceViewModel
    }
}