package com.moa.salary.app.presentation.ui.onboarding.login

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.salary.app.core.model.setting.OAuthType
import com.moa.salary.app.data.repository.AuthRepository
import com.moa.salary.app.data.repository.HomeRepository
import com.moa.salary.app.data.repository.OnboardingRepository
import com.moa.salary.app.data.repository.TokenRepository
import com.moa.salary.app.presentation.bus.MoaSideEffectBus
import com.moa.salary.app.presentation.extensions.determineHomeNavigation
import com.moa.salary.app.presentation.extensions.execute
import com.moa.salary.app.presentation.manager.FcmTokenManager
import com.moa.salary.app.presentation.manager.KakaoLoginManager
import com.moa.salary.app.presentation.model.MoaSideEffect
import com.moa.salary.app.presentation.model.OnboardingNavigation
import com.moa.salary.app.presentation.model.PosthogEvent
import com.moa.salary.app.presentation.model.RootNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
    private val authRepository: AuthRepository,
    private val onboardingRepository: OnboardingRepository,
    private val homeRepository: HomeRepository,
    private val tokenRepository: TokenRepository,
) : ViewModel() {

    fun clickKakao(activity: Activity) {
        viewModelScope.launch {
            try {
                val kakaoTokenDeferred = async {
                    KakaoLoginManager.loginWithKakao(activity)
                }
                val fcmTokenDeferred = async {
                    FcmTokenManager.getFcmToken()
                }

                val kakaoToken = kakaoTokenDeferred.await()
                val fcmDeviceToken = fcmTokenDeferred.await()

                postToken(
                    idToken = kakaoToken,
                    fcmDeviceToken = fcmDeviceToken
                )
            } catch (e: Exception) {
                Toast.makeText(
                    activity,
                    "로그인 실패: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun postToken(
        idToken: String,
        fcmDeviceToken: String,
    ) {
        suspend {
            authRepository.postToken(
                idToken = idToken,
                fcmDeviceToken = fcmDeviceToken,
            )
        }.execute(
            bus = moaSideEffectBus,
            scope = viewModelScope,
            onRetry = { postToken(idToken, fcmDeviceToken) }
        ) {
            saveToken(it)
        }
    }

    private fun saveToken(accessToken: String) {
        suspend {
            tokenRepository.saveAccessToken(accessToken)
        }.execute(scope = viewModelScope) {
            getOnboardingStatus()
        }
    }

    private fun getOnboardingStatus() {
        viewModelScope.launch {
            runCatching {
                onboardingRepository.getOnboardingStatus()
            }.onSuccess { onboardingStatus ->
                if (onboardingStatus.hasRequiredTermsAgreed) {
                    getHome()
                    return@launch
                }

                sendEvent(LoginEvent.ClickLogin(OAuthType.KAKAO))

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
                toast()
            }
        }
    }

    private fun getHome() {
        viewModelScope.launch {
            runCatching {
                homeRepository.getHome()
            }.onSuccess {
                sendEvent(LoginEvent.ClickLogin(OAuthType.KAKAO))

                val completedWorkDay = homeRepository.getCompletedWorkDay()
                val homeNavigation = it.determineHomeNavigation(completedWorkDay)
                navigate(RootNavigation.Home(homeNavigation))
            }.onFailure {
                toast()
            }
        }
    }

    private fun navigate(destination: RootNavigation) {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(destination))
        }
    }

    private fun toast() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Toast("일시적인 오류가 발생했어요"))
        }
    }

    private fun sendEvent(event: PosthogEvent) {
        event.sendEvent()
    }
}