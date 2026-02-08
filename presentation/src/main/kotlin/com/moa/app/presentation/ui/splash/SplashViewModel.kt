package com.moa.app.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.data.repository.OnboardingRepository
import com.moa.app.data.repository.TokenRepository
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.model.OnboardingNavigation
import com.moa.app.presentation.model.RootNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
    private val tokenRepository: TokenRepository,
    private val onboardingRepository: OnboardingRepository,
) : ViewModel() {
    fun getOnboardingStatus() {
        viewModelScope.launch {
            val token = tokenRepository.getAccessToken()

            if (token == null) {
                navigate(RootNavigation.Onboarding())
                return@launch
            }

            val onboardingStatus = onboardingRepository.getOnboardingStatus()

            if (onboardingStatus.hasRequiredTermsAgreed) {
                navigate(RootNavigation.Home)
                return@launch
            }

            val nickName = onboardingStatus.nickName
            if (nickName == null) {
                navigate(RootNavigation.Onboarding(OnboardingNavigation.Nickname()))
                return@launch
            } else {
                navigate(
                    RootNavigation.Onboarding(
                        OnboardingNavigation.Nickname(
                            args = OnboardingNavigation.Nickname.NicknameNavigationArgs(
                                nickName = nickName
                            )
                        )
                    )
                )
            }

            val payroll = onboardingStatus.payroll
            if (payroll == null) {
                navigate(RootNavigation.Onboarding(OnboardingNavigation.Salary()))
                return@launch
            } else {
                navigate(
                    RootNavigation.Onboarding(
                        OnboardingNavigation.Salary(
                            OnboardingNavigation.Salary.SalaryNavigationArgs(
                                salary = payroll.salary,
                                salaryType = payroll.salaryType,
                            )
                        )
                    )
                )
            }

            val workPolicy = onboardingStatus.workPolicy
            if (workPolicy == null) {
                navigate(RootNavigation.Onboarding(OnboardingNavigation.WorkSchedule()))
                return@launch
            } else {
                navigate(
                    RootNavigation.Onboarding(
                        OnboardingNavigation.WorkSchedule(
                            OnboardingNavigation.WorkSchedule.WorkScheduleNavigationArgs(
                                workScheduleDays = workPolicy.workScheduleDays,
                                time = workPolicy.time,
                            )
                        )
                    )
                )
            }
        }
    }

    private fun navigate(destination: RootNavigation) {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(destination))
        }
    }
}
