package com.moa.app.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.core.extensions.toHourMinuteOrNull
import com.moa.app.data.local.PreferencesDataStore
import com.moa.app.data.remote.model.response.HomeResponse
import com.moa.app.data.remote.model.response.HomeType
import com.moa.app.data.repository.HomeRepository
import com.moa.app.data.repository.OnboardingRepository
import com.moa.app.data.repository.TokenRepository
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.HomeNavigation
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.model.OnboardingNavigation
import com.moa.app.presentation.model.RootNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
    private val tokenRepository: TokenRepository,
    private val onboardingRepository: OnboardingRepository,
    private val homeRepository: HomeRepository,
    private val preferencesDataStore: PreferencesDataStore,
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
                onboardingStatus.profile?.paydayDay?.let { paydayDay ->
                    preferencesDataStore.setPaydayDay(paydayDay)
                }

                if (onboardingStatus.hasRequiredTermsAgreed) {
                    navigateToHome()
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

    private fun navigate(destination: RootNavigation) {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(destination))
        }
    }

    private suspend fun navigateToHome() {
        try {
            val response = homeRepository.getHome()
            val homeNavigation = response.toHomeNavigation()
            navigate(RootNavigation.Home(homeNavigation))
        } catch (e: Exception) {
            navigate(RootNavigation.Home(HomeNavigation.BeforeWork()))
        }
    }

    private fun HomeResponse.toHomeNavigation(): HomeNavigation {
        val clockIn = clockInTime?.toHourMinuteOrNull()
        val clockOut = clockOutTime?.toHourMinuteOrNull()

        val startHour = clockIn?.first ?: 9
        val startMinute = clockIn?.second ?: 0
        val endHour = clockOut?.first ?: 18
        val endMinute = clockOut?.second ?: 0

        val isWorkDay = type != HomeType.NONE
        val isOnVacation = type == HomeType.VACATION

        val now = LocalTime.now()
        val clockInLocalTime = LocalTime.of(startHour, startMinute)
        val clockOutLocalTime = LocalTime.of(endHour, endMinute)

        val isOvernightShift = clockOutLocalTime < clockInLocalTime

        val isBeforeWork: Boolean
        val isAfterWork: Boolean

        if (isOvernightShift) {
            isBeforeWork = now >= clockOutLocalTime && now < clockInLocalTime
            isAfterWork = false
        } else {
            isBeforeWork = now < clockInLocalTime
            isAfterWork = now >= clockOutLocalTime
        }

        return when {
            isBeforeWork -> HomeNavigation.BeforeWork()
            isAfterWork -> HomeNavigation.AfterWork(
                todayEarnedSalary = dailyPay,
                startHour = startHour,
                startMinute = startMinute,
                endHour = endHour,
                endMinute = endMinute,
                isOnVacation = isOnVacation,
            )
            else -> HomeNavigation.Working(
                startHour = startHour,
                startMinute = startMinute,
                endHour = endHour,
                endMinute = endMinute,
                dailyPay = dailyPay,
                isOnVacation = isOnVacation,
                isWorkDay = isWorkDay,
            )
        }
    }
}
