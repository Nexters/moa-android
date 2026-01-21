package com.moa.app.presentation.ui.onboarding.workschedule

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.model.Time
import com.moa.app.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
data class WorkScheduleUiState(
    val selectedWorkScheduleDays: ImmutableSet<WorkScheduleDay> = persistentSetOf(),
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
    ),
    val showTimeBottomSheet: Time? = null,
)

enum class WorkScheduleDay(val title: String) {
    MONDAY("월"),
    TUESDAY("화"),
    WEDNESDAY("수"),
    THURSDAY("목"),
    FRIDAY("금"),
    SATURDAY("토"),
    SUNDAY("일"),
}

@HiltViewModel
class WorkScheduleViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
) : ViewModel() {
    private val _uiState = MutableStateFlow(WorkScheduleUiState())
    val uiState = _uiState.asStateFlow()

    fun onIntent(intent: WorkScheduleIntent) {
        when (intent) {
            WorkScheduleIntent.ClickBack -> back()
            is WorkScheduleIntent.ClickWorkScheduleDay -> clickWorkScheduleDay(intent.day)
            is WorkScheduleIntent.ShowTimeBottomSheet -> showTimeBottomSheet(intent.time)
            is WorkScheduleIntent.SetTime -> setTime(intent.time)
            WorkScheduleIntent.ClickNext -> next()
        }
    }

    private fun back() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(Screen.Back))
        }
    }

    private fun clickWorkScheduleDay(day: WorkScheduleDay) {
        val currentSet = _uiState.value.selectedWorkScheduleDays.toMutableSet()
        if (currentSet.contains(day)) {
            currentSet.remove(day)
        } else {
            currentSet.add(day)
        }
        _uiState.value = _uiState.value.copy(
            selectedWorkScheduleDays = currentSet.toImmutableSet(),
        )
    }

    private fun showTimeBottomSheet(time: Time?) {
        _uiState.value = _uiState.value.copy(
            showTimeBottomSheet = time,
        )
    }

    private fun setTime(time: Time) {
        val currentTimes = _uiState.value.times.toMutableList()
        val index = currentTimes.indexOfFirst { it::class == time::class }
        if (index != -1) {
            currentTimes[index] = time
            _uiState.value = _uiState.value.copy(
                times = currentTimes.toImmutableList(),
            )
        }
    }

    private fun next() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(Screen.WidgetGuide))
        }
    }
}