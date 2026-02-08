package com.moa.app.presentation.ui.setting.workinfo

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.core.model.onboarding.Payroll
import com.moa.app.core.model.onboarding.Time
import com.moa.app.core.model.onboarding.WorkPolicy
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.model.OnboardingNavigation
import com.moa.app.presentation.model.RootNavigation
import com.moa.app.presentation.model.SettingNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
data class WorkInfoUiState(
    val oauthType: String = "카카오",
    val salaryType: Payroll.SalaryType = Payroll.SalaryType.MONTHLY,
    val salary: String = "4000000",
    val salaryDate: String = "25일",
    val workPlace: String = "집계리아",
    val workScheduleDays: ImmutableList<WorkPolicy.WorkScheduleDay> = persistentListOf(
        WorkPolicy.WorkScheduleDay.MON,
        WorkPolicy.WorkScheduleDay.TUE,
        WorkPolicy.WorkScheduleDay.WED,
        WorkPolicy.WorkScheduleDay.THU,
        WorkPolicy.WorkScheduleDay.FRI,
    ),
    val time: Time = Time(
        startHour = 9,
        startMinute = 0,
        endHour = 18,
        endMinute = 0,
    ),
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
            WorkInfoIntent.ClickWorkplace -> workPlace()
            WorkInfoIntent.ClickWorkSchedule -> workSchedule()
        }
    }

    private fun back() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(SettingNavigation.Back))
        }
    }

    private fun salary() {
        viewModelScope.launch {
            moaSideEffectBus.emit(
                MoaSideEffect.Navigate(
                    destination = RootNavigation.Onboarding(
                        startDestination = OnboardingNavigation.Salary(
                            args = OnboardingNavigation.Salary.SalaryNavigationArgs(
                                salary = _uiState.value.salary,
                                salaryType = _uiState.value.salaryType,
                                isOnboarding = false,
                            )
                        )
                    )
                )
            )
        }
    }

    private fun salaryDate() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(SettingNavigation.SalaryDate))
        }
    }

    private fun workPlace() {
        viewModelScope.launch {
            moaSideEffectBus.emit(
                MoaSideEffect.Navigate(
                    SettingNavigation.WorkPlace(_uiState.value.workPlace)
                )
            )
        }
    }

    private fun workSchedule() {
        viewModelScope.launch {
            moaSideEffectBus.emit(
                MoaSideEffect.Navigate(
                    destination = RootNavigation.Onboarding(
                        startDestination = OnboardingNavigation.WorkSchedule(
                            args = OnboardingNavigation.WorkSchedule.WorkScheduleNavigationArgs(
                                workScheduleDays = _uiState.value.workScheduleDays,
                                time = _uiState.value.time,
                                isOnboarding = false,
                            )
                        )
                    )
                )
            )
        }
    }
}