package com.moa.app.presentation.ui.setting.workinfo

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.core.model.setting.WorkInfo
import com.moa.app.data.repository.SettingRepository
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.extensions.execute
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.model.OnboardingNavigation
import com.moa.app.presentation.model.RootNavigation
import com.moa.app.presentation.model.SettingNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
data class WorkInfoUiState(
    val workInfo: WorkInfo? = null
)

@HiltViewModel
class WorkInfoViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
    private val settingRepository: SettingRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkInfoUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getWorkInfo()
    }

    fun onIntent(intent: WorkInfoIntent) {
        when (intent) {
            WorkInfoIntent.ClickBack -> back()
            WorkInfoIntent.ClickSalary -> salary()
            WorkInfoIntent.ClickSalaryDate -> salaryDate()
            WorkInfoIntent.ClickCompanyName -> companyName()
            WorkInfoIntent.ClickWorkSchedule -> workSchedule()
        }
    }

    private fun getWorkInfo() {
        suspend {
            settingRepository.getWorkInfo()
        }.execute(
            bus = moaSideEffectBus,
            scope = viewModelScope,
        ) {
            _uiState.value = _uiState.value.copy(workInfo = it)
        }
    }

    private fun back() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(SettingNavigation.Back))
        }
    }

    private fun salary() {
        viewModelScope.launch {
            val payroll = _uiState.value.workInfo?.payroll
            if (payroll != null) {
                moaSideEffectBus.emit(
                    MoaSideEffect.Navigate(
                        destination = RootNavigation.Onboarding(
                            startDestination = OnboardingNavigation.Salary(
                                args = OnboardingNavigation.Salary.SalaryNavigationArgs(
                                    salary = payroll.salary,
                                    salaryType = payroll.salaryType,
                                    isOnboarding = false,
                                )
                            )
                        )
                    )
                )
            }
        }
    }

    private fun salaryDate() {
        viewModelScope.launch {
            val payroll = _uiState.value.workInfo?.payroll
            if (payroll != null) {
                moaSideEffectBus.emit(MoaSideEffect.Navigate(SettingNavigation.SalaryDay(payroll.paydayDay)))
            }
        }
    }

    private fun companyName() {
        viewModelScope.launch {
            val companyName = _uiState.value.workInfo?.companyName
            if (companyName != null) {
                moaSideEffectBus.emit(
                    MoaSideEffect.Navigate(
                        SettingNavigation.CompanyName(
                            companyName
                        )
                    )
                )
            }
        }
    }

    private fun workSchedule() {
        viewModelScope.launch {
            val workPolicy = _uiState.value.workInfo?.workPolicy
            if (workPolicy != null) {
                moaSideEffectBus.emit(
                    MoaSideEffect.Navigate(
                        destination = RootNavigation.Onboarding(
                            startDestination = OnboardingNavigation.WorkSchedule(
                                args = OnboardingNavigation.WorkSchedule.WorkScheduleNavigationArgs(
                                    workScheduleDays = workPolicy.workScheduleDays,
                                    time = workPolicy.time,
                                    isOnboarding = false,
                                )
                            )
                        )
                    )
                )
            }
        }
    }
}