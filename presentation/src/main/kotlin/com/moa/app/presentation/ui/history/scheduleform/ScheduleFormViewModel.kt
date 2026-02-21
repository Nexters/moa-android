package com.moa.app.presentation.ui.history.scheduleform

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.core.model.history.LocalDateModel
import com.moa.app.core.model.history.Schedule
import com.moa.app.core.model.history.ScheduleType
import com.moa.app.core.model.onboarding.Time
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.model.RootNavigation
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class ScheduleInputType {
    VACATION,
    WORK,
}

@Stable
data class ScheduleFormUiState(
    val isEditMode: Boolean = false,
    val scheduleId: Long = 0,
    val date: LocalDateModel = LocalDateModel(2026, 1, 1),
    val scheduleType: ScheduleInputType = ScheduleInputType.WORK,
    val time: Time = Time(9, 0, 18, 0),
    val showDateBottomSheet: Boolean = false,
    val showTimeBottomSheet: Boolean = false,
)

sealed interface ScheduleFormIntent {
    data object ClickBack : ScheduleFormIntent
    data class SelectScheduleType(val type: ScheduleInputType) : ScheduleFormIntent
    data class SetDate(val date: LocalDateModel) : ScheduleFormIntent
    data class SetTime(val time: Time) : ScheduleFormIntent
    data class ShowDateBottomSheet(val visible: Boolean) : ScheduleFormIntent
    data class ShowTimeBottomSheet(val visible: Boolean) : ScheduleFormIntent
    data object ClickCancel : ScheduleFormIntent
    data object ClickConfirm : ScheduleFormIntent
}

@HiltViewModel(assistedFactory = ScheduleFormViewModel.Factory::class)
class ScheduleFormViewModel @AssistedInject constructor(
    @Assisted private val initialDate: LocalDateModel,
    @Assisted private val schedule: Schedule?,
    private val moaSideEffectBus: MoaSideEffectBus,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        if (schedule != null) {
            ScheduleFormUiState(
                isEditMode = true,
                scheduleId = schedule.id,
                date = schedule.date,
                scheduleType = when (schedule.type) {
                    ScheduleType.VACATION -> ScheduleInputType.VACATION
                    else -> ScheduleInputType.WORK
                },
                time = schedule.time ?: Time(9, 0, 18, 0),
            )
        } else {
            ScheduleFormUiState(
                isEditMode = false,
                date = initialDate,
            )
        }
    )
    val uiState = _uiState.asStateFlow()

    fun onIntent(intent: ScheduleFormIntent) {
        when (intent) {
            ScheduleFormIntent.ClickBack -> back()
            is ScheduleFormIntent.SelectScheduleType -> selectType(intent.type)
            is ScheduleFormIntent.SetDate -> setDate(intent.date)
            is ScheduleFormIntent.SetTime -> setTime(intent.time)
            is ScheduleFormIntent.ShowDateBottomSheet -> showDateBottomSheet(intent.visible)
            is ScheduleFormIntent.ShowTimeBottomSheet -> showTimeBottomSheet(intent.visible)
            ScheduleFormIntent.ClickCancel -> cancel()
            ScheduleFormIntent.ClickConfirm -> confirm()
        }
    }

    private fun back() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(RootNavigation.Back))
        }
    }

    private fun selectType(type: ScheduleInputType) {
        _uiState.update { it.copy(scheduleType = type) }
    }

    private fun setDate(date: LocalDateModel) {
        _uiState.update { it.copy(date = date, showDateBottomSheet = false) }
    }

    private fun setTime(time: Time) {
        _uiState.update { it.copy(time = time, showTimeBottomSheet = false) }
    }

    private fun showDateBottomSheet(visible: Boolean) {
        _uiState.update { it.copy(showDateBottomSheet = visible) }
    }

    private fun showTimeBottomSheet(visible: Boolean) {
        _uiState.update { it.copy(showTimeBottomSheet = visible) }
    }

    private fun cancel() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(RootNavigation.Back))
        }
    }

    private fun confirm() {
        viewModelScope.launch {
            if (_uiState.value.isEditMode) {
                // TODO: update API
            } else {
                // TODO: create API
            }
            moaSideEffectBus.emit(MoaSideEffect.Navigate(RootNavigation.Back))
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(initialDate: LocalDateModel, schedule: Schedule?): ScheduleFormViewModel
    }
}