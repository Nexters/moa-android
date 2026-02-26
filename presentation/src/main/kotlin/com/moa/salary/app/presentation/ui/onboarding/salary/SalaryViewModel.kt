package com.moa.salary.app.presentation.ui.onboarding.salary

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.salary.app.core.model.onboarding.Payroll
import com.moa.salary.app.data.repository.OnboardingRepository
import com.moa.salary.app.data.repository.SettingRepository
import com.moa.salary.app.presentation.bus.MoaSideEffectBus
import com.moa.salary.app.presentation.extensions.execute
import com.moa.salary.app.presentation.model.MoaSideEffect
import com.moa.salary.app.presentation.model.OnboardingNavigation
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Stable
data class SalaryUiState(
    val selectedSalaryType: Payroll.SalaryType = Payroll.SalaryType.MONTHLY,
    val salaryTextField: TextFieldState = TextFieldState(),
)

@HiltViewModel(assistedFactory = SalaryViewModel.Factory::class)
class SalaryViewModel @AssistedInject constructor(
    @Assisted private val args: OnboardingNavigation.Salary.SalaryNavigationArgs,
    private val moaSideEffectBus: MoaSideEffectBus,
    private val onboardingRepository: OnboardingRepository,
    private val settingRepository: SettingRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        SalaryUiState(
            selectedSalaryType = args.salaryType,
            salaryTextField = TextFieldState(args.salary),
        )
    )
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
            moaSideEffectBus.emit(MoaSideEffect.Navigate(OnboardingNavigation.Back))
        }
    }

    private fun selectSalaryType(salaryType: Payroll.SalaryType) {
        _uiState.value = _uiState.value.copy(
            selectedSalaryType = salaryType
        )
    }

    private fun next() {
        if (args.isOnboarding) {
            nextIfIsOnboarding()
        } else {
            nextIfIsNotOnboarding()
        }
    }

    private fun nextIfIsOnboarding() {
        suspend {
            onboardingRepository.patchPayroll(
                Payroll(
                    salaryType = uiState.value.selectedSalaryType,
                    salary = uiState.value.salaryTextField.text.toString(),
                )
            )
        }.execute(
            bus = moaSideEffectBus,
            scope = viewModelScope,
            onRetry = { nextIfIsOnboarding() },
        ) {
            viewModelScope.launch {
                moaSideEffectBus.emit(MoaSideEffect.Navigate(OnboardingNavigation.WorkSchedule()))
            }
        }
    }

    private fun nextIfIsNotOnboarding() {
        suspend {
            settingRepository.patchPayroll(
                Payroll(
                    salaryType = uiState.value.selectedSalaryType,
                    salary = uiState.value.salaryTextField.text.toString(),
                )
            )
        }.execute(
            bus = moaSideEffectBus,
            scope = viewModelScope,
            onRetry = { nextIfIsNotOnboarding() },
        ) {
            viewModelScope.launch {
                moaSideEffectBus.emit(MoaSideEffect.RefreshHome)
                moaSideEffectBus.emit(MoaSideEffect.Navigate(OnboardingNavigation.Back))
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(args: OnboardingNavigation.Salary.SalaryNavigationArgs): SalaryViewModel
    }
}