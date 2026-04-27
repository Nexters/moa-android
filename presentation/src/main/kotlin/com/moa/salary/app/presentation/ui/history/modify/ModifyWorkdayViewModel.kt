package com.moa.salary.app.presentation.ui.history.modify

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.salary.app.core.extensions.makeTimeString
import com.moa.salary.app.core.extensions.toLocalDate
import com.moa.salary.app.core.model.onboarding.Time
import com.moa.salary.app.core.model.work.WorkdayType
import com.moa.salary.app.data.repository.WorkdayRepository
import com.moa.salary.app.presentation.bus.MoaSideEffectBus
import com.moa.salary.app.presentation.extensions.execute
import com.moa.salary.app.presentation.model.HistoryNavigation
import com.moa.salary.app.presentation.model.MoaSideEffect
import com.moa.salary.app.presentation.model.PosthogEvent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

@Stable
data class ModifyWorkdayUiState(
    val date: LocalDate,
    val isFromCalendar: Boolean,
    val joinedAt: LocalDate,
    val selectedWorkdayType: WorkdayType,
    val time: Time,
    val showDateBottomSheet: Boolean = false,
    val showTimeBottomSheet: Boolean = false,
){
    val diffTimeString = buildString {
        val diffTimePair = time.calculateTimeDiff()
        val hours = diffTimePair.first
        val minutes = diffTimePair.second

        if(hours > 0) {
            append("${hours}시간 ")
        }
        if(minutes > 0){
            append("${minutes}분 ")
        }
    }
}

@HiltViewModel(assistedFactory = ModifyWorkdayViewModel.Factory::class)
class ModifyWorkdayViewModel @AssistedInject constructor(
    @Assisted private val args: HistoryNavigation.ModifyWorkday.ModifyWorkdayArgs,
    private val moaSideEffectBus: MoaSideEffectBus,
    private val workdayRepository: WorkdayRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ModifyWorkdayUiState(
            date = args.date.toLocalDate(),
            isFromCalendar = args.joinedAt != null,
            joinedAt = args.joinedAt?.toLocalDate() ?: LocalDate.now(),
            selectedWorkdayType = args.workdayType,
            time = args.time,
        )
    )
    val uiState = _uiState.asStateFlow()

    fun onIntent(intent: ModifyWorkdayIntent) {
        when (intent) {
            ModifyWorkdayIntent.ClickBack -> back()
            is ModifyWorkdayIntent.SetShowDateBottomSheet -> setShowDateBottomSheet(intent.show)
            is ModifyWorkdayIntent.SetDate -> setDate(intent.date)
            is ModifyWorkdayIntent.SetWorkdayType -> setWorkdayType(intent.type)
            is ModifyWorkdayIntent.SetShowTimeBottomSheet -> setShowTimeBottomSheet(intent.show)
            is ModifyWorkdayIntent.SetTime -> setTime(intent.time)
            ModifyWorkdayIntent.ClickCancel -> cancel()
            ModifyWorkdayIntent.ClickConfirm -> confirm()
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
        suspend {
            workdayRepository.getWorkday(date.toString())
        }.execute(
            bus = moaSideEffectBus,
            scope = viewModelScope,
            onRetry = { setDate(date) }
        ) { workday ->
            _uiState.value = _uiState.value.copy(
                date = workday.date.toLocalDate(),
                selectedWorkdayType = if (workday.type == WorkdayType.NONE) WorkdayType.WORK else workday.type,
                time = Time(
                    startHour = workday.startHour ?: 9,
                    startMinute = workday.startMinute ?: 0,
                    endHour = workday.endHour ?: 18,
                    endMinute = workday.endMinute ?: 0,
                )
            )
        }
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
                        currentState.time.startMinute,
                    )
                } else {
                    null
                },
                clockOutTime = if (currentState.selectedWorkdayType == WorkdayType.WORK) {
                    makeTimeString(
                        currentState.time.endHour,
                        currentState.time.endMinute,
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
            sendEvent(ModifyWorkdayEvent.CompleteModify(currentState.selectedWorkdayType.name))
            back()
        }
    }

    private fun sendEvent(event : PosthogEvent) {
        event.sendEvent()
    }

    @AssistedFactory
    interface Factory {
        fun create(args: HistoryNavigation.ModifyWorkday.ModifyWorkdayArgs): ModifyWorkdayViewModel
    }
}