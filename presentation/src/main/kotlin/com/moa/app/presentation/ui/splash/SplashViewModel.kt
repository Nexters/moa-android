package com.moa.app.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.data.repository.OnboardingRepository
import com.moa.app.presentation.bus.MoaSideEffectBus
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
        viewModelScope.launch {
            val onboardingState = onboardingRepository.fetchOnboardingState()

            when {
                !onboardingState.loginCompleted -> {
                    moaSideEffectBus.emit(
                        MoaSideEffect.Navigate(
                            RootNavigation.Onboarding()
                        )
                    )
                }

                !onboardingState.nickNameCompleted -> {
                    moaSideEffectBus.emit(
                        MoaSideEffect.Navigate(
                            RootNavigation.Onboarding(
                                OnboardingNavigation.Nickname()
                            )
                        )
                    )
                }

                !onboardingState.workPlaceCompleted -> {
                    moaSideEffectBus.emit(
                        MoaSideEffect.Navigate(
                            RootNavigation.Onboarding(
                                OnboardingNavigation.WorkPlace()
                            )
                        )
                    )
                }

                !onboardingState.salaryCompleted -> {
                    moaSideEffectBus.emit(
                        MoaSideEffect.Navigate(
                            RootNavigation.Onboarding(
                                OnboardingNavigation.Salary()
                            )
                        )
                    )
                }

                !onboardingState.workScheduleCompleted -> {
                    moaSideEffectBus.emit(
                        MoaSideEffect.Navigate(
                            RootNavigation.Onboarding(
                                OnboardingNavigation.WorkSchedule()
                            )
                        )
                    )
                }

                else -> {
                    moaSideEffectBus.emit(MoaSideEffect.Navigate(RootNavigation.Home))
                }
            }
        }
    }
}