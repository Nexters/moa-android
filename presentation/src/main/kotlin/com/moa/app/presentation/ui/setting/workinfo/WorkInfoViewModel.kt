package com.moa.app.presentation.ui.setting.workinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.navigation.SettingNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkInfoViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
) : ViewModel() {

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

    }

    private fun workSchedule() {

    }

    private fun workPlace() {

    }
}