package com.moa.salary.app.presentation.ui.history.modify

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.salary.app.core.extensions.makeTimeString
import com.moa.salary.app.core.extensions.toLocalDate
import com.moa.salary.app.core.extensions.toTime
import com.moa.salary.app.core.model.onboarding.Time
import com.moa.salary.app.core.model.work.WorkdayType
import com.moa.salary.app.data.repository.WorkdayRepository
import com.moa.salary.app.presentation.bus.MoaSideEffectBus
import com.moa.salary.app.presentation.extensions.execute
import com.moa.salary.app.presentation.model.HistoryNavigation
import com.moa.salary.app.presentation.model.MoaSideEffect
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

@Stable
data class ModifyScheduleUiState(
    val isEditMode: Boolean,
    val date: LocalDate,
    val selectedWorkdayType: WorkdayType,
    val time: Time,
    val showDateBottomSheet: Boolean = false,
    val showTimeBottomSheet: Boolean = false,
)

@HiltViewModel(assistedFactory = ModifyScheduleViewModel.Factory::class)
class ModifyScheduleViewModel @AssistedInject constructor(
    @Assisted private val args: HistoryNavigation.ModifySchedule.ModifyScheduleArgs,
    private val moaSideEffectBus: MoaSideEffectBus,
    private val workdayRepository: WorkdayRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ModifyScheduleUiState(
            isEditMode = args.schedule.type != WorkdayType.NONE,
            date = args.currentDate.toLocalDate(),
            selectedWorkdayType = args.schedule.type,
            time = args.schedule.workTime.toTime(),
        )
    )
    val uiState = _uiState.asStateFlow()

    fun onIntent(intent: ModifyScheduleIntent) {
        when (intent) {
            ModifyScheduleIntent.ClickBack -> back()
            is ModifyScheduleIntent.SetShowDateBottomSheet -> setShowDateBottomSheet(intent.show)
            is ModifyScheduleIntent.SetDate -> setDate(intent.date)
            is ModifyScheduleIntent.SetWorkdayType -> setWorkdayType(intent.type)
            is ModifyScheduleIntent.SetShowTimeBottomSheet -> setShowTimeBottomSheet(intent.show)
            is ModifyScheduleIntent.SetTime -> setTime(intent.time)
            ModifyScheduleIntent.ClickCancel -> cancel()
            ModifyScheduleIntent.ClickConfirm -> confirm()
        }
    }

    private fun back() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(HistoryNavigation.Back))
        }
    }

    private fun setShowDateBottomSheet(show: Boolean) {
        _uiState.value = _uiState.value.copy(showDateBottomSheet = show)
    }

    private fun setDate(date: LocalDate) {
        _uiState.value = _uiState.value.copy(date = date)
    }

    private fun setWorkdayType(type: WorkdayType) {
        _uiState.value = _uiState.value.copy(selectedWorkdayType = type)
    }

    private fun setShowTimeBottomSheet(show: Boolean) {
        _uiState.value = _uiState.value.copy(showTimeBottomSheet = show)
    }

    private fun setTime(time: Time) {
        _uiState.value = _uiState.value.copy(time = time)
    }

    private fun cancel() {
        back()
    }

    private fun confirm() {
        val currentState = _uiState.value
        suspend {
            workdayRepository.updateWorkday(
                date = currentState.date.toString(),
                clockInTime = if (currentState.selectedWorkdayType == WorkdayType.WORK) {
                    makeTimeString(
                        currentState.time.startHour,
                        currentState.time.startMinute
                    )
                } else {
                    null
                },
                clockOutTime = if (currentState.selectedWorkdayType == WorkdayType.WORK) {
                    makeTimeString(
                        currentState.time.endHour,
                        currentState.time.endMinute
                    )
                } else {
                    null
                },
                type = currentState.selectedWorkdayType,
            )
        }.execute(
            bus = moaSideEffectBus,
            scope = viewModelScope,
            onRetry = { confirm() }
        ) {
            back()
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(args: HistoryNavigation.ModifySchedule.ModifyScheduleArgs): ModifyScheduleViewModel
    }
}