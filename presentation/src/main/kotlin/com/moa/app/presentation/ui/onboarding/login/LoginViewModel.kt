package com.moa.app.presentation.ui.onboarding.login

import androidx.lifecycle.ViewModel
import com.moa.app.presentation.bus.MoaSideEffectBus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
) : ViewModel() {
    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.SaveAccessToken -> saveAccessToken(intent.token)
        }
    }

    private fun saveAccessToken(token : String) {

    }
}