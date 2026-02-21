package com.moa.app.presentation.ui.onboarding.login

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.data.repository.AuthRepository
import com.moa.app.data.repository.OnboardingRepository
import com.moa.app.data.repository.TokenRepository
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.extensions.execute
import com.moa.app.presentation.manager.FcmTokenManager
import com.moa.app.presentation.manager.KakaoLoginManager
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.model.OnboardingNavigation
import com.moa.app.presentation.model.RootNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
    private val authRepository: AuthRepository,
    private val onboardingRepository: OnboardingRepository,
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
                    navigate(RootNavigation.Home)
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
}