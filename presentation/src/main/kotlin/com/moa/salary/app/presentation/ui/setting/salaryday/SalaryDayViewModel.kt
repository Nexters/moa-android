package com.moa.salary.app.presentation.ui.setting.salaryday

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.salary.app.data.repository.SettingRepository
import com.moa.salary.app.presentation.bus.MoaSideEffectBus
import com.moa.salary.app.presentation.extensions.execute
import com.moa.salary.app.presentation.model.MoaSideEffect
import com.moa.salary.app.presentation.model.SettingNavigation
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Stable
data class SalaryDayUiState(
    val salaryDay: Int,
    val showSalaryDayBottomSheet: Boolean = false,
)

@HiltViewModel(assistedFactory = SalaryDayViewModel.Factory::class)
class SalaryDayViewModel @AssistedInject constructor(
    @Assisted day: Int,
    private val moaSideEffectBus: MoaSideEffectBus,
    private val settingRepository: SettingRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SalaryDayUiState(salaryDay = day))
    val uiState = _uiState.asStateFlow()

    fun onIntent(intent: SalaryDateIntent) {
        when (intent) {
            is SalaryDateIntent.ClickBack -> back()
            is SalaryDateIntent.ShowSalaryDayBottomSheet -> showSalaryDayBottomSheet(intent.visible)
            is SalaryDateIntent.SetSalaryDay -> setSalaryDay(intent.day)
            is SalaryDateIntent.Confirm -> confirm()
        }
    }

    private fun back() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(SettingNavigation.Back))
        }
    }

    private fun showSalaryDayBottomSheet(visible: Boolean) {
        _uiState.value = _uiState.value.copy(
            showSalaryDayBottomSheet = visible,
        )
    }

    private fun setSalaryDay(day: Int) {
        _uiState.value = _uiState.value.copy(salaryDay = day)
    }

    private fun confirm() {
        suspend {
            settingRepository.patchPaydayDay(_uiState.value.salaryDay)
        }.execute(
            bus = moaSideEffectBus,
            scope = viewModelScope,
            onRetry = { setSalaryDay(_uiState.value.salaryDay) },
        ) {
            back()
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(day: Int): SalaryDayViewModel
    }

}