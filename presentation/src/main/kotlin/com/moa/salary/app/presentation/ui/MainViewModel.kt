package com.moa.salary.app.presentation.ui

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.salary.app.data.repository.TokenRepository
import com.moa.salary.app.presentation.bus.MoaSideEffectBus
import com.moa.salary.app.presentation.model.MoaDialogProperties
import com.moa.salary.app.presentation.model.MoaSideEffect
import com.moa.salary.app.presentation.model.OnboardingNavigation
import com.moa.salary.app.presentation.model.RootNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
data class MainUiState(
    val isLoading: Boolean = false,
    val dialog: MoaDialogProperties? = null,
    val errorRetry: (() -> Unit)? = null,
    val toastMessage: String? = null,
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
    private val tokenRepository: TokenRepository,
) : ViewModel() {
    val moaSideEffects = moaSideEffectBus.sideEffects

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeSessionExpired()
    }

    fun onIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.SetDialog -> setDialog(intent.dialog)
            is MainIntent.SetLoading -> setLoading(intent.visible)
            is MainIntent.SetErrorRetry -> setErrorRetry(intent.retry)
            is MainIntent.SetToast -> setToast(intent.message)
        }
    }

    private fun observeSessionExpired() {
        viewModelScope.launch {
            tokenRepository.sessionExpired.collect {
                moaSideEffectBus.emit(
                    MoaSideEffect.Navigate(
                        destination = RootNavigation.Onboarding(
                            startDestination = OnboardingNavigation.Login
                        )
                    )
                )
            }
        }
    }

    private fun setDialog(dialog: MoaDialogProperties?) {
        _uiState.value = _uiState.value.copy(dialog = dialog)
    }

    private fun setLoading(visible: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = visible)
    }

    private fun setErrorRetry(retry: (() -> Unit)?) {
        _uiState.value = _uiState.value.copy(errorRetry = retry)
    }

    private fun setToast(message: String?) {
        _uiState.value = _uiState.value.copy(toastMessage = message)
    }
}
