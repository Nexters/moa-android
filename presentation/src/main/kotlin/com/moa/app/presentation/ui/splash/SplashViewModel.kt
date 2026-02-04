package com.moa.app.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.data.repository.OnboardingRepository
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.extensions.execute
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.navigation.OnboardingNavigation
import com.moa.app.presentation.navigation.RootNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
    private val onboardingRepository: OnboardingRepository,
) : ViewModel() {
    fun checkLoginStatus() {
        suspend {
            onboardingRepository.fetchOnboardingState()
        }.execute(scope = viewModelScope) { onboardingState ->
            when {
                !onboardingState.loginCompleted -> {
                    navigate(RootNavigation.Onboarding())
                }

                !onboardingState.nickNameCompleted -> {
                    navigate(RootNavigation.Onboarding(OnboardingNavigation.Nickname()))
                }

                !onboardingState.workPlaceCompleted -> {
                    navigate(RootNavigation.Onboarding(OnboardingNavigation.WorkPlace()))
                }

                !onboardingState.salaryCompleted -> {
                    navigate(RootNavigation.Onboarding(OnboardingNavigation.Salary()))
                }

                !onboardingState.workScheduleCompleted -> {
                    navigate(RootNavigation.Onboarding(OnboardingNavigation.WorkSchedule()))
                }

                else -> {
                    navigate(RootNavigation.Home)
                }
            }
        }
    }

    private fun navigate(destination: RootNavigation) {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(destination))
        }
    }
}