package com.moa.app.presentation.ui.setting.workinfo

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.model.SalaryType
import com.moa.app.presentation.model.Time
import com.moa.app.presentation.model.WorkScheduleDay
import com.moa.app.presentation.navigation.OnboardingNavigation
import com.moa.app.presentation.navigation.RootNavigation
import com.moa.app.presentation.navigation.SettingNavigation
import com.moa.app.presentation.ui.onboarding.OnboardingNavigationArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
data class WorkInfoUiState(
    val oauthType: String = "카카오",
    val salaryType: SalaryType = SalaryType.Monthly,
    val salary: String = "4000000",
    val salaryDate: String = "25일",
    val workPlace: String = "집계리아",
    val workScheduleDays: ImmutableSet<WorkScheduleDay> = persistentSetOf(
        WorkScheduleDay.MONDAY,
        WorkScheduleDay.TUESDAY,
        WorkScheduleDay.WEDNESDAY,
        WorkScheduleDay.THURSDAY,
        WorkScheduleDay.FRIDAY,
    ),
    val times: ImmutableList<Time> = persistentListOf(
        Time.Work(
            startHour = 9,
            startMinute = 0,
            endHour = 18,
            endMinute = 0,
        ),
        Time.Lunch(
            startHour = 12,
            startMinute = 0,
            endHour = 13,
            endMinute = 0,
        )
    )
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
                            args = OnboardingNavigationArgs(
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
                    destination = RootNavigation.Onboarding(
                        startDestination = OnboardingNavigation.WorkPlace(
                            args = OnboardingNavigationArgs(
                                workPlace = _uiState.value.workPlace,
                                isOnboarding = false,
                            )
                        )
                    )
                )
            )
        }
    }

    private fun workSchedule() {

    }
}