package com.moa.app.presentation.ui.setting.workinfo

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.navigation.SettingNavigation
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
data class WorkInfoUiState(
    val oauthType: String = "카카오",
    val salary: String = "월급 · 400만원",
    val salaryDate: String = "25일",
    val workPlace: String = "집계리아",
    val workScheduleDays: String = "월, 화, 수, 목, 금",
    val workTime: String = "09:00~18:00",
    val lunchTime: String = "12:00~13:00",
)

@HiltViewModel
class WorkInfoViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkInfoUiState())
    val uiState = _uiState.asStateFlow()

    fun onIntent(intent: WorkInfoIntent) {
        when (intent) {
            WorkInfoIntent.ClickBack -> back()
            WorkInfoIntent.ClickSalary -> salary()
            WorkInfoIntent.ClickSalaryDate -> salaryDate()
            WorkInfoIntent.ClickWorkSchedule -> workSchedule()
            WorkInfoIntent.ClickWorkplace -> workPlace()
        }
    }

    private fun back() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(SettingNavigation.Back))
        }
    }

    private fun salary() {

    }

    private fun salaryDate() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(SettingNavigation.SalaryDate))
        }
    }

    private fun workSchedule() {

    }

    private fun workPlace() {

    }
}