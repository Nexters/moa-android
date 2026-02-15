package com.moa.app.presentation.ui.onboarding.login

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.data.repository.OnboardingRepository
import com.moa.app.data.repository.TokenRepository
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.extensions.execute
import com.moa.app.presentation.manager.FcmTokenManager
import com.moa.app.presentation.manager.KakaoLoginManager
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.model.OnboardingNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
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
            onboardingRepository.postToken(
                idToken = idToken,
                fcmDeviceToken = fcmDeviceToken,
            )
        }.execute(
            bus = moaSideEffectBus,
            scope = viewModelScope,
        ) {
            saveToken(it)
        }
    }

    private fun saveToken(accessToken: String) {
        suspend {
            tokenRepository.saveAccessToken(accessToken)
        }.execute(scope = viewModelScope) {
            viewModelScope.launch {
                moaSideEffectBus.emit(MoaSideEffect.Navigate(OnboardingNavigation.Nickname()))
            }
        }
    }
}