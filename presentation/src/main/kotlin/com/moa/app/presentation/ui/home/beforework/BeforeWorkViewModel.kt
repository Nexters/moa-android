package com.moa.app.presentation.ui.home.beforework

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.core.model.onboarding.WorkPolicy
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.navigation.HomeNavigation
import com.moa.app.presentation.ui.home.beforework.model.BeforeWorkIntent
import com.moa.app.presentation.ui.home.beforework.model.BeforeWorkUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

private const val TIME_CHECK_INTERVAL_MS = 1000L

@HiltViewModel(assistedFactory = BeforeWorkViewModel.Factory::class)
class BeforeWorkViewModel @AssistedInject constructor(
    @Assisted private val args: HomeNavigation.BeforeWork,
    private val moaSideEffectBus: MoaSideEffectBus,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(args: HomeNavigation.BeforeWork): BeforeWorkViewModel
    }

    private val workScheduleDays = listOf(
        WorkPolicy.WorkScheduleDay.MON,
        WorkPolicy.WorkScheduleDay.TUE,
        WorkPolicy.WorkScheduleDay.WED,
        WorkPolicy.WorkScheduleDay.THU,
        WorkPolicy.WorkScheduleDay.FRI,
    )

    private val _uiState = MutableStateFlow(
        BeforeWorkUiState(
            todayEarnedSalary = args.todayEarnedSalary,
            isWorkDay = checkIsWorkDay(),
        )
    )
    val uiState: StateFlow<BeforeWorkUiState> = _uiState.asStateFlow()

    private var hasAutoClockInTriggered = false

    init {
        if (_uiState.value.isWorkDay) {
            startAutoClockInChecker()
        }
    }

    private fun checkIsWorkDay(): Boolean {
        val today = LocalDate.now().dayOfWeek
        val todayWorkScheduleDay = when (today) {
            DayOfWeek.MONDAY -> WorkPolicy.WorkScheduleDay.MON
            DayOfWeek.TUESDAY -> WorkPolicy.WorkScheduleDay.TUE
            DayOfWeek.WEDNESDAY -> WorkPolicy.WorkScheduleDay.WED
            DayOfWeek.THURSDAY -> WorkPolicy.WorkScheduleDay.THU
            DayOfWeek.FRIDAY -> WorkPolicy.WorkScheduleDay.FRI
            DayOfWeek.SATURDAY -> WorkPolicy.WorkScheduleDay.SAT
            DayOfWeek.SUNDAY -> WorkPolicy.WorkScheduleDay.SUN
        }
        return workScheduleDays.contains(todayWorkScheduleDay)
    }

    fun onIntent(intent: BeforeWorkIntent) {
        when (intent) {
            BeforeWorkIntent.ClickWorkTime -> onClickWorkTime()
            BeforeWorkIntent.ClickEarlyClockIn -> onClickEarlyClockIn()
            BeforeWorkIntent.ClickVacation -> onClickVacation()
            BeforeWorkIntent.ClickClockInOnDayOff -> onClickClockInOnDayOff()
            BeforeWorkIntent.DismissTimeBottomSheet -> onDismissTimeBottomSheet()
            is BeforeWorkIntent.UpdateWorkTime -> onUpdateWorkTime(intent)
        }
    }

    private fun startAutoClockInChecker() {
        viewModelScope.launch {
            while (true) {
                checkAutoClockIn()
                delay(TIME_CHECK_INTERVAL_MS)
            }
        }
    }

    private suspend fun checkAutoClockIn() {
        if (hasAutoClockInTriggered) return

        val currentTime = LocalTime.now()
        val state = _uiState.value
        val clockInTime = LocalTime.of(state.startHour, state.startMinute)

        if (currentTime >= clockInTime) {
            hasAutoClockInTriggered = true
            navigateToWorking()
        }
    }

    private fun onClickWorkTime() {
        _uiState.update { it.copy(showTimeBottomSheet = true) }
    }

    private fun onDismissTimeBottomSheet() {
        _uiState.update { it.copy(showTimeBottomSheet = false) }
    }

    private fun onUpdateWorkTime(intent: BeforeWorkIntent.UpdateWorkTime) {
        hasAutoClockInTriggered = false

        _uiState.update { state ->
            state.copy(
                startHour = intent.startHour,
                startMinute = intent.startMinute,
                endHour = intent.endHour,
                endMinute = intent.endMinute,
                showTimeBottomSheet = false,
            )
        }
    }

    private fun onClickEarlyClockIn() {
        hasAutoClockInTriggered = true
        val now = LocalTime.now()
        navigateToWorking(
            startHour = now.hour,
            startMinute = now.minute,
        )
    }

    private fun navigateToWorking(
        startHour: Int? = null,
        startMinute: Int? = null,
        endHour: Int? = null,
        endMinute: Int? = null,
        isOnVacation: Boolean = false,
    ) {
        viewModelScope.launch {
            val state = _uiState.value
            moaSideEffectBus.emit(
                MoaSideEffect.Navigate(
                    HomeNavigation.Working(
                        startHour = startHour ?: state.startHour,
                        startMinute = startMinute ?: state.startMinute,
                        endHour = endHour ?: state.endHour,
                        endMinute = endMinute ?: state.endMinute,
                        isOnVacation = isOnVacation,
                        isWorkDay = state.isWorkDay,
                    )
                )
            )
        }
    }

    private fun onClickVacation() {
        hasAutoClockInTriggered = true
        val now = LocalTime.now()
        val endTime = now.plusHours(3)
        navigateToWorking(
            startHour = now.hour,
            startMinute = now.minute,
            endHour = endTime.hour,
            endMinute = endTime.minute,
            isOnVacation = true,
        )
    }

    private fun onClickClockInOnDayOff() {
        val now = LocalTime.now()
        val endTime = now.plusHours(3)
        navigateToWorking(
            startHour = now.hour,
            startMinute = now.minute,
            endHour = endTime.hour,
            endMinute = endTime.minute,
        )
    }
}