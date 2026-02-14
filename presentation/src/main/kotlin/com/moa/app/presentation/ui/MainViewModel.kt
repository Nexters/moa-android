package com.moa.app.presentation.ui

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.MoaDialogProperties
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@Stable
data class MainUiState(
    val isLoading: Boolean = false,
    val dialog: MoaDialogProperties? = null,
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
        }
    }

    private fun setDialog(dialog: MoaDialogProperties?) {
        _uiState.value = _uiState.value.copy(dialog = dialog)
    }

    private fun setLoading(visible: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = visible)
    }
}