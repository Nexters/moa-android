package com.moa.app.presentation.ui.onboarding.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.data.repository.TokenRepository
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.extensions.execute
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.model.OnboardingNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
    private val tokenRepository: TokenRepository,
) : ViewModel() {
    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.PostToken -> postToken(intent.token)
        }
    }

    private fun postToken(token: String) {
        suspend {
            tokenRepository.postToken(token)
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