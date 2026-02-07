package com.moa.app.presentation.ui.onboarding.login

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.data.repository.TokenRepository
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.extensions.execute
import com.moa.app.presentation.manager.FcmTokenManager
import com.moa.app.presentation.manager.KakaoLoginManager
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.model.OnboardingNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @param:ApplicationContext private val context : Context,
    private val moaSideEffectBus: MoaSideEffectBus,
    private val tokenRepository: TokenRepository,
) : ViewModel() {
    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.ClickKakao -> clickKakao()
        }
    }

    private fun clickKakao() {
        viewModelScope.launch {
            try {
                val kakaoTokenDeferred = async {
                    KakaoLoginManager.loginWithKakao(context)
                }
                val fcmTokenDeferred = async {
                    FcmTokenManager.getFcmToken()
                }

                val kakaoToken = kakaoTokenDeferred.await()
                val fcmToken = fcmTokenDeferred.await()

                postToken(
                        accessToken = kakaoToken,
                        fcmToken = fcmToken
                    )
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "로그인 실패: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun postToken(
        accessToken: String,
        fcmToken: String,
    ) {
        suspend {
            tokenRepository.postToken(
                accessToken = accessToken,
                fcmToken = fcmToken,
            )
        }.execute(
            bus = moaSideEffectBus,
            scope = viewModelScope,
        ) {
            saveToken(it)
        }
    }

    private fun saveToken(jwt: String) {
        suspend {
            tokenRepository.saveAccessToken(jwt)
        }.execute(scope = viewModelScope) {
            viewModelScope.launch {
                moaSideEffectBus.emit(MoaSideEffect.Navigate(OnboardingNavigation.Nickname()))
            }
        }
    }
}