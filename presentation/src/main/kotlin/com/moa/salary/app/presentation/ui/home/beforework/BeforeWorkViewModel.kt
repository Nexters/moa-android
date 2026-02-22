package com.moa.salary.app.presentation.ui.home.beforework

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.salary.app.core.extensions.makeTimeString
import com.moa.salary.app.core.extensions.toHourMinuteOrNull
import com.moa.salary.app.data.remote.model.response.HomeType
import com.moa.salary.app.data.repository.HomeRepository
import com.moa.salary.app.data.repository.WorkdayRepository
import com.moa.salary.app.presentation.bus.MoaSideEffectBus
import com.moa.salary.app.presentation.extensions.execute
import com.moa.salary.app.presentation.model.HomeNavigation
import com.moa.salary.app.presentation.model.MoaSideEffect
import com.moa.salary.app.presentation.ui.home.beforework.model.BeforeWorkIntent
import com.moa.salary.app.presentation.ui.home.beforework.model.BeforeWorkUiState
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
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private const val TIME_CHECK_INTERVAL_MS = 1000L

@HiltViewModel(assistedFactory = BeforeWorkViewModel.Factory::class)
class BeforeWorkViewModel @AssistedInject constructor(
    @Assisted private val args: HomeNavigation.BeforeWork,
    private val moaSideEffectBus: MoaSideEffectBus,
    private val homeRepository: HomeRepository,
    private val workdayRepository: WorkdayRepository,
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(args: HomeNavigation.BeforeWork): BeforeWorkViewModel
    }

    private val _uiState = MutableStateFlow(
        BeforeWorkUiState(
            todayEarnedSalary = args.todayEarnedSalary,
        )
    )
    val uiState: StateFlow<BeforeWorkUiState> = _uiState.asStateFlow()

    private var hasAutoClockInTriggered = false

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        suspend {
            homeRepository.getHome()
        }.execute(
            bus = moaSideEffectBus,
            scope = viewModelScope,
            onRetry = { loadHomeData() },
        ) { homeResponse ->
            val clockInTime = homeResponse.clockInTime?.toHourMinuteOrNull()
            val clockOutTime = homeResponse.clockOutTime?.toHourMinuteOrNull()
            val isWorkDay = homeResponse.type != HomeType.NONE
            val isOnVacation = homeResponse.type == HomeType.VACATION

            _uiState.update { state ->
                state.copy(
                    location = homeResponse.workplace,
                    workedEarnings = homeResponse.workedEarnings,
                    standardSalary = homeResponse.standardSalary,
                    dailyPay = homeResponse.dailyPay,
                    startHour = clockInTime?.first ?: state.startHour,
                    startMinute = clockInTime?.second ?: state.startMinute,
                    endHour = clockOutTime?.first ?: state.endHour,
                    endMinute = clockOutTime?.second ?: state.endMinute,
                    isWorkDay = isWorkDay,
                    isOnVacation = isOnVacation,
                )
            }

            if (isWorkDay && !hasAutoClockInTriggered) {
                startAutoClockInChecker()
            }
        }
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

    private fun checkAutoClockIn() {
        if (hasAutoClockInTriggered) return

        val currentTime = LocalTime.now()
        val state = _uiState.value
        val clockInTime = LocalTime.of(state.startHour, state.startMinute)
        val clockOutTime = LocalTime.of(state.endHour, state.endMinute)

        val isOvernightShift = clockOutTime < clockInTime

        if (isOvernightShift) {
            val isWorking = currentTime >= clockInTime || currentTime < clockOutTime
            if (isWorking) {
                hasAutoClockInTriggered = true
                navigateToWorking(isOnVacation = state.isOnVacation)
            }
        } else {
            if (currentTime >= clockOutTime) {
                hasAutoClockInTriggered = true
                navigateToAfterWork()
                return
            }

            if (currentTime >= clockInTime) {
                hasAutoClockInTriggered = true
                navigateToWorking(isOnVacation = state.isOnVacation)
            }
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
        val state = _uiState.value

        val registeredStartMinutes = state.startHour * 60 + state.startMinute
        val registeredEndMinutes = state.endHour * 60 + state.endMinute
        val workDurationMinutes = registeredEndMinutes - registeredStartMinutes
        val endTime = now.plusMinutes(workDurationMinutes.toLong())

        updateWorkTimeApi(
            startHour = now.hour,
            startMinute = now.minute,
            endHour = endTime.hour,
            endMinute = endTime.minute,
        )

        navigateToWorking(
            startHour = now.hour,
            startMinute = now.minute,
            endHour = endTime.hour,
            endMinute = endTime.minute,
        )
    }

    private fun updateWorkTimeApi(
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int,
        type: String = "WORK",
    ) {
        val clockInTime = makeTimeString(startHour, startMinute)
        val clockOutTime = makeTimeString(endHour, endMinute)

        suspend {
            homeRepository.saveAdjustedWorkTime(clockInTime, clockOutTime)
        }.execute(scope = viewModelScope) {}

        suspend {
            val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
            workdayRepository.updateWorkTime(today, clockInTime, clockOutTime, type)
        }.execute(scope = viewModelScope) {}
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
                        dailyPay = state.dailyPay,
                        isOnVacation = isOnVacation,
                        isWorkDay = state.isWorkDay,
                    )
                )
            )
        }
    }

    private fun navigateToAfterWork() {
        viewModelScope.launch {
            val state = _uiState.value
            moaSideEffectBus.emit(
                MoaSideEffect.Navigate(
                    HomeNavigation.AfterWork(
                        todayEarnedSalary = state.dailyPay,
                        startHour = state.startHour,
                        startMinute = state.startMinute,
                        endHour = state.endHour,
                        endMinute = state.endMinute,
                        isOnVacation = false,
                    )
                )
            )
        }
    }

    private fun onClickVacation() {
        hasAutoClockInTriggered = true
        val state = _uiState.value
        val now = LocalTime.now()

        val registeredStartMinutes = state.startHour * 60 + state.startMinute
        val registeredEndMinutes = state.endHour * 60 + state.endMinute
        val workDurationMinutes = registeredEndMinutes - registeredStartMinutes
        val endTime = now.plusMinutes(workDurationMinutes.toLong())

        updateWorkTimeApi(
            startHour = now.hour,
            startMinute = now.minute,
            endHour = endTime.hour,
            endMinute = endTime.minute,
            type = "VACATION",
        )

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

        updateWorkTimeApi(
            startHour = now.hour,
            startMinute = now.minute,
            endHour = endTime.hour,
            endMinute = endTime.minute,
        )

        navigateToWorking(
            startHour = now.hour,
            startMinute = now.minute,
            endHour = endTime.hour,
            endMinute = endTime.minute,
        )
    }
}