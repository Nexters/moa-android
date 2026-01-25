package com.moa.app.presentation.ui.setting.workinfo

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.navigation.SettingNavigation
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Stable
data class WorkInfoUiState(
    val oauthType: String,
    val salary: String,
    val salaryDate: String,
    val workPlace: String,
    val workScheduleDays: String,
    val workTime: String,
    val lunchTime: String,
)

@HiltViewModel(assistedFactory = WorkInfoViewModel.Factory::class)
class WorkInfoViewModel @AssistedInject constructor(
    @Assisted private val args: SettingNavigation.WorkInfo.WorkInfoArgs,
    private val moaSideEffectBus: MoaSideEffectBus,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        WorkInfoUiState(
            oauthType = args.oauthType,
            salary = "${args.salaryType.title} · ${args.salary}",
            salaryDate = "${args.salaryDate}일",
            workPlace = args.workPlace,
            workScheduleDays = args.workScheduleDays.joinToString(", ") { it.title },
            workTime = "${args.workStartTime}~${args.workEndTime}",
            lunchTime = "${args.lunchStartTime}~${args.lunchEndTime}",
        )
    )
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

    @AssistedFactory
    interface Factory {
        fun create(args: SettingNavigation.WorkInfo.WorkInfoArgs): WorkInfoViewModel
    }
}