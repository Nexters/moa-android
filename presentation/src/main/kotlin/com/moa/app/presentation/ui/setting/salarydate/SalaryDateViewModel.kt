package com.moa.app.presentation.ui.setting.salarydate

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.navigation.SettingNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
data class SalaryDateUiState(
    val salaryDate: Int = 25,
    val showSalaryDateBottomSheet: Boolean = false,
)

@HiltViewModel
class SalaryDateViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SalaryDateUiState())
    val uiState = _uiState.asStateFlow()

    fun onIntent(intent: SalaryDateIntent) {
        when (intent) {
            is SalaryDateIntent.ClickBack -> back()
            is SalaryDateIntent.ShowSalaryDateBottomSheet -> showSalaryDateBottomSheet(intent.visible)
            is SalaryDateIntent.SetSalaryDate -> setSalaryDate(intent.date)
        }
    }

    private fun back() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(SettingNavigation.Back))
        }
    }

    private fun showSalaryDateBottomSheet(visible: Boolean) {
        _uiState.value = _uiState.value.copy(
            showSalaryDateBottomSheet = visible,
        )
    }

    private fun setSalaryDate(date: Int) {
        _uiState.value = _uiState.value.copy(
            salaryDate = date,
            showSalaryDateBottomSheet = false,
        )
    }
}