package com.moa.app.presentation.ui.home.beforework

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.data.remote.model.response.HomeType
import com.moa.app.data.repository.HomeRepository
import com.moa.app.data.repository.WorkdayRepository
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.HomeNavigation
import com.moa.app.presentation.model.MoaSideEffect
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
        Log.d(TAG, "[BeforeWork] ViewModel created")
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "[BeforeWork] Loading home data...")
                val homeResponse = homeRepository.getHome()
                Log.d(TAG, "[BeforeWork] API response: type=${homeResponse.type}, clockIn=${homeResponse.clockInTime}, clockOut=${homeResponse.clockOutTime}")

                val clockInTime = homeResponse.clockInTime?.let { parseTime(it) }
                val clockOutTime = homeResponse.clockOutTime?.let { parseTime(it) }
                val isWorkDay = homeResponse.type != HomeType.NONE

                Log.d(TAG, "[BeforeWork] Parsed times: clockIn=$clockInTime, clockOut=$clockOutTime, isWorkDay=$isWorkDay")

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
                    )
                }

                if (isWorkDay && !hasAutoClockInTriggered) {
                    Log.d(TAG, "[BeforeWork] Starting auto clock-in checker")
                    startAutoClockInChecker()
                } else {
                    Log.d(TAG, "[BeforeWork] Skipping auto clock-in: isWorkDay=$isWorkDay, hasAutoClockInTriggered=$hasAutoClockInTriggered")
                }
            } catch (e: Exception) {
                Log.e(TAG, "[BeforeWork] Failed to load home data", e)
                moaSideEffectBus.emit(MoaSideEffect.Failure(e) { loadHomeData() })
            }
        }
    }

    private fun parseTime(time: String): Pair<Int, Int>? {
        return try {
            val parts = time.split(":")
            if (parts.size >= 2) {
                Pair(parts[0].toInt(), parts[1].toInt())
            } else null
        } catch (e: Exception) {
            null
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

    private suspend fun checkAutoClockIn() {
        if (hasAutoClockInTriggered) return

        val currentTime = LocalTime.now()
        val state = _uiState.value
        val clockInTime = LocalTime.of(state.startHour, state.startMinute)
        val clockOutTime = LocalTime.of(state.endHour, state.endMinute)

        val isOvernightShift = clockOutTime < clockInTime

        Log.d(TAG, "[BeforeWork] Time check: current=$currentTime, clockIn=$clockInTime, clockOut=$clockOutTime, isOvernightShift=$isOvernightShift")

        if (isOvernightShift) {
            val isWorking = currentTime >= clockInTime || currentTime < clockOutTime
            if (isWorking) {
                Log.d(TAG, "[BeforeWork] Overnight shift: navigating to Working")
                hasAutoClockInTriggered = true
                navigateToWorking()
            }
        } else {
            if (currentTime >= clockOutTime) {
                Log.d(TAG, "[BeforeWork] Auto navigation: current=$currentTime >= clockOut=$clockOutTime, navigating to AfterWork")
                hasAutoClockInTriggered = true
                navigateToAfterWork()
                return
            }

            if (currentTime >= clockInTime) {
                Log.d(TAG, "[BeforeWork] Auto navigation: current=$currentTime >= clockIn=$clockInTime, navigating to Working")
                hasAutoClockInTriggered = true
                navigateToWorking()
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

        updateWorkTimeApi(
            startHour = now.hour,
            startMinute = now.minute,
            endHour = state.endHour,
            endMinute = state.endMinute,
        )

        navigateToWorking(
            startHour = now.hour,
            startMinute = now.minute,
        )
    }

    private fun updateWorkTimeApi(
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int,
        type: String = "WORK",
    ) {
        viewModelScope.launch {
            val clockInTime = String.format("%02d:%02d", startHour, startMinute)
            val clockOutTime = String.format("%02d:%02d", endHour, endMinute)

            try {
                homeRepository.saveAdjustedWorkTime(clockInTime, clockOutTime)
            } catch (e: Exception) {
                Log.e(TAG, "[BeforeWork] Failed to save adjusted work time locally", e)
            }

            try {
                val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
                Log.d(TAG, "[BeforeWork] Updating work time (PUT): $today, type=$type, $clockInTime ~ $clockOutTime")
                workdayRepository.updateWorkTime(today, clockInTime, clockOutTime, type)
            } catch (e: Exception) {
                Log.e(TAG, "[BeforeWork] Failed to update work time API", e)
            }
        }
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

    companion object {
        private const val TAG = "HomeNavigation"
    }
}