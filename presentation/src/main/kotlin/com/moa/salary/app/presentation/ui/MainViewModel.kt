package com.moa.salary.app.presentation.ui

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import com.moa.salary.app.presentation.bus.MoaSideEffectBus
import com.moa.salary.app.presentation.model.MoaDialogProperties
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    moaSideEffectBus: MoaSideEffectBus,
) : ViewModel() {
    val moaSideEffects = moaSideEffectBus.sideEffects

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    fun onIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.SetDialog -> setDialog(intent.dialog)
            is MainIntent.SetLoading -> setLoading(intent.visible)
            is MainIntent.SetErrorRetry -> setErrorRetry(intent.retry)
            is MainIntent.SetToast -> setToast(intent.message)
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