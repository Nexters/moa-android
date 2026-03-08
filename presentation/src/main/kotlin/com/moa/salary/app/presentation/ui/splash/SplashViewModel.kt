package com.moa.salary.app.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.salary.app.data.repository.HomeRepository
import com.moa.salary.app.data.repository.OnboardingRepository
import com.moa.salary.app.data.repository.TokenRepository
import com.moa.salary.app.presentation.bus.MoaSideEffectBus
import com.moa.salary.app.presentation.extensions.determineHomeNavigation
import com.moa.salary.app.presentation.model.MoaSideEffect
import com.moa.salary.app.presentation.model.OnboardingNavigation
import com.moa.salary.app.presentation.model.RootNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
    private val tokenRepository: TokenRepository,
    private val onboardingRepository: OnboardingRepository,
    private val homeRepository: HomeRepository,
) : ViewModel() {
    fun getOnboardingStatus() {
        viewModelScope.launch {
            val token = tokenRepository.getAccessToken()

            if (token == null) {
                navigate(RootNavigation.Onboarding())
                return@launch
            }

            runCatching {
                onboardingRepository.getOnboardingStatus()
            }.onSuccess { onboardingStatus ->
                if (onboardingStatus.hasRequiredTermsAgreed) {
                    getHome()
                    return@launch
                }

                val nickName = onboardingStatus.profile?.nickname
                if (nickName == null) {
                    navigate(RootNavigation.Onboarding(OnboardingNavigation.Nickname()))
                    return@launch
                } else {
                    navigate(
                        RootNavigation.Onboarding(
                            OnboardingNavigation.Nickname(
                                args = OnboardingNavigation.Nickname.NicknameNavigationArgs(
                                    nickname = nickName
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
            }.onFailure {
                navigate(RootNavigation.Onboarding())
            }
        }
    }

    private fun getHome() {
        viewModelScope.launch {
            runCatching {
                homeRepository.getHome()
            }.onSuccess {
                val homeNavigation = it.determineHomeNavigation()
                navigate(RootNavigation.Home(homeNavigation))
            }.onFailure {
                navigate(RootNavigation.Onboarding())
            }
        }
    }

    private fun navigate(destination: RootNavigation) {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(destination))
        }
    }
}
