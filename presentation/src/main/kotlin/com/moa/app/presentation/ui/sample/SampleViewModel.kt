package com.moa.app.presentation.ui.sample

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SampleUiState(
    val test: String = "",
)

@HiltViewModel
class SampleViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SampleUiState())
    val uiState = _uiState.asStateFlow()

    fun onIntent(intent: SampleIntent) {
        when (intent) {
            is SampleIntent.Click -> click()
        }
    }

    private fun click() {
        _uiState.value = uiState.value.copy(
            test = "clicked"
        )
    }
}