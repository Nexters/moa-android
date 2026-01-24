package com.moa.app.presentation.ui.onboarding.salary

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.navigation.OnboardingScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
data class SalaryUiState(
    val selectedSalaryType: SalaryType = SalaryType.Monthly,
    val salaryTextField: TextFieldState = TextFieldState(),
)

enum class SalaryType(val title: String) {
    Monthly("월급"),
    Yearly("연봉")
}

@HiltViewModel
class SalaryViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SalaryUiState())
    val uiState = _uiState.asStateFlow()

    fun onIntent(intent: SalaryIntent) {
        when (intent) {
            SalaryIntent.ClickBack -> back()
            is SalaryIntent.SelectSalaryType -> selectSalaryType(intent.type)
            SalaryIntent.ClickNext -> next()
        }
    }

    private fun back() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(OnboardingScreen.Back))
        }
    }

    private fun selectSalaryType(salaryType: SalaryType) {
        _uiState.value = _uiState.value.copy(
            selectedSalaryType = salaryType
        )
    }

    private fun next() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(OnboardingScreen.WorkSchedule))
        }
    }
}