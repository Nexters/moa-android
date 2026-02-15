package com.moa.app.presentation.ui.home.working

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.navigation.HomeNavigation
import com.moa.app.presentation.ui.home.working.model.WorkStatus
import com.moa.app.presentation.ui.home.working.model.WorkingIntent
import com.moa.app.presentation.ui.home.working.model.WorkingUiState
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
import java.time.LocalTime

private const val TOOLTIP_COUNT = 3
private const val TOOLTIP_ROTATION_INTERVAL_MS = 5000L
private const val SALARY_PER_SECOND = 1L
private const val MAX_COIN_HEIGHT_FRACTION = 0.7f

@HiltViewModel(assistedFactory = WorkingViewModel.Factory::class)
class WorkingViewModel @AssistedInject constructor(
    @Assisted private val args: HomeNavigation.Working,
    private val moaSideEffectBus: MoaSideEffectBus,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(args: HomeNavigation.Working): WorkingViewModel
    }

    private val _uiState: MutableStateFlow<WorkingUiState>
    val uiState: StateFlow<WorkingUiState>

    private var clockInTime: LocalTime = LocalTime.now()
    private var hasShownConfettiInCurrentCycle: Boolean = false
    private var lastCycleSecond: Int = -1
    private var isEndTimeCheckEnabled: Boolean = false
    private var hasNavigatedToAfterWork: Boolean = false

    init {
        val now = LocalTime.now()
        _uiState = MutableStateFlow(
            WorkingUiState(
                startHour = args.startHour,
                startMinute = args.startMinute,
                endHour = args.endHour,
                endMinute = args.endMinute,
                currentHour = now.hour,
                currentMinute = now.minute,
                currentSecond = now.second,
                isOnVacation = args.isOnVacation,
                workStatus = if (args.isOnVacation) WorkStatus.VACATION else WorkStatus.WORKING,
            )
        )
        uiState = _uiState.asStateFlow()

        initializeClockInTime()
        startTimer()
        startTooltipRotation()
    }

    fun onIntent(intent: WorkingIntent) {
        when (intent) {
            WorkingIntent.ClickAdjustSchedule -> onClickAdjustSchedule()
            WorkingIntent.DismissScheduleAdjustBottomSheet -> onDismissScheduleAdjustBottomSheet()
            WorkingIntent.DismissTimeBottomSheet -> onDismissTimeBottomSheet()
            WorkingIntent.DismissConfetti -> onDismissConfetti()
            WorkingIntent.SelectVacation -> onSelectVacation()
            WorkingIntent.SelectEndWork -> onSelectEndWork()
            WorkingIntent.SelectAdjustTime -> onSelectAdjustTime()
            is WorkingIntent.UpdateWorkTime -> onUpdateWorkTime(intent)
            WorkingIntent.ClickContinueWorking -> onClickContinueWorking()
            WorkingIntent.ClickCompleteWork -> onClickCompleteWork()
            WorkingIntent.ClickWorkTimeEdit -> onClickWorkTimeEdit()
            WorkingIntent.DismissMoreWorkBottomSheet -> onDismissMoreWorkBottomSheet()
            WorkingIntent.DismissWorkTimeEditBottomSheet -> onDismissWorkTimeEditBottomSheet()
            WorkingIntent.ClickTodayVacation -> onClickTodayVacation()
            is WorkingIntent.ConfirmMoreWork -> onConfirmMoreWork(intent)
            is WorkingIntent.ConfirmWorkTimeEdit -> onConfirmWorkTimeEdit(intent)
        }
    }

    private fun initializeClockInTime() {
        clockInTime = LocalTime.now()
        updateElapsedTime()
    }

    private fun startTimer() {
        viewModelScope.launch {
            delay(500L)
            isEndTimeCheckEnabled = true
            while (true) {
                updateElapsedTime()
                delay(500L)
            }
        }
    }

    private fun updateElapsedTime() {
        if (_uiState.value.showWorkCompletionOverlay) return

        val now = LocalTime.now()
        val elapsedSeconds = java.time.Duration.between(clockInTime, now).seconds.toInt()

        val hours = elapsedSeconds / 3600
        val minutes = (elapsedSeconds % 3600) / 60
        val seconds = elapsedSeconds % 60

        if (seconds < lastCycleSecond) {
            hasShownConfettiInCurrentCycle = false
        }
        lastCycleSecond = seconds

        val shouldShowConfetti = seconds >= 59 && !hasShownConfettiInCurrentCycle

        if (shouldShowConfetti) {
            hasShownConfettiInCurrentCycle = true
        }

        _uiState.update { state ->
            val newTodaySalary = elapsedSeconds.toLong() * SALARY_PER_SECOND
            val endTime = LocalTime.of(state.endHour, state.endMinute)
            val remainingSeconds = java.time.Duration.between(now, endTime).seconds.toInt().coerceAtLeast(0)
            val remainingHours = remainingSeconds / 3600

            state.copy(
                elapsedSeconds = seconds,
                elapsedMinutes = minutes,
                elapsedHours = hours,
                currentHour = now.hour,
                currentMinute = now.minute,
                currentSecond = now.second,
                todaySalary = newTodaySalary,
                remainingHours = remainingHours,
                showConfetti = if (shouldShowConfetti) true else state.showConfetti,
            )
        }

        checkEndTimeReached(now)
    }

    private fun checkEndTimeReached(now: LocalTime) {
        if (!isEndTimeCheckEnabled || hasNavigatedToAfterWork) return

        val state = _uiState.value
        val startTime = LocalTime.of(state.startHour, state.startMinute)
        val endTime = LocalTime.of(state.endHour, state.endMinute)

        val isEndTimeNextDay = endTime < startTime
        val hasReachedEndTime = if (isEndTimeNextDay) {
            now >= endTime && now < startTime
        } else {
            now >= endTime
        }

        if (hasReachedEndTime) {
            hasNavigatedToAfterWork = true
            _uiState.update { it.copy(showWorkCompletionOverlay = true) }
        }
    }

    private fun startTooltipRotation() {
        viewModelScope.launch {
            delay(TOOLTIP_ROTATION_INTERVAL_MS)
            while (true) {
                _uiState.update { state ->
                    val nextIndex = (state.currentTooltipIndex + 1) % TOOLTIP_COUNT
                    state.copy(currentTooltipIndex = nextIndex)
                }
                delay(TOOLTIP_ROTATION_INTERVAL_MS)
            }
        }
    }

    private fun onClickAdjustSchedule() {
        _uiState.update { it.copy(showScheduleAdjustBottomSheet = true) }
    }

    private fun onDismissScheduleAdjustBottomSheet() {
        _uiState.update { it.copy(showScheduleAdjustBottomSheet = false) }
    }

    private fun onDismissTimeBottomSheet() {
        _uiState.update { it.copy(showTimeBottomSheet = false) }
    }

    private fun onDismissConfetti() {
        _uiState.update { it.copy(showConfetti = false) }
    }

    private fun onSelectVacation() {
        _uiState.update { state ->
            state.copy(
                showScheduleAdjustBottomSheet = false,
                isOnVacation = true,
                workStatus = WorkStatus.VACATION,
            )
        }
    }

    private fun onSelectEndWork() {
        val now = LocalTime.now()
        _uiState.update {
            it.copy(
                showScheduleAdjustBottomSheet = false,
                showWorkCompletionOverlay = true,
                endHour = now.hour,
                endMinute = now.minute,
            )
        }
    }

    private fun navigateToAfterWork() {
        viewModelScope.launch {
            val state = _uiState.value
            moaSideEffectBus.emit(
                MoaSideEffect.Navigate(
                    HomeNavigation.AfterWork(
                        todayEarnedSalary = state.todaySalary,
                        startHour = state.startHour,
                        startMinute = state.startMinute,
                        endHour = state.endHour,
                        endMinute = state.endMinute,
                        isOnVacation = state.isOnVacation,
                    )
                )
            )
        }
    }

    private fun onSelectAdjustTime() {
        _uiState.update { state ->
            state.copy(
                showScheduleAdjustBottomSheet = false,
                showTimeBottomSheet = true,
            )
        }
    }

    private fun onUpdateWorkTime(intent: WorkingIntent.UpdateWorkTime) {
        clockInTime = LocalTime.of(intent.startHour, intent.startMinute)

        _uiState.update { state ->
            state.copy(
                startHour = intent.startHour,
                startMinute = intent.startMinute,
                endHour = intent.endHour,
                endMinute = intent.endMinute,
                showTimeBottomSheet = false,
            )
        }

        updateElapsedTime()
    }

    private fun onClickContinueWorking() {
        _uiState.update { it.copy(showMoreWorkBottomSheet = true) }
    }

    private fun onClickCompleteWork() {
        _uiState.update { it.copy(showWorkCompletionOverlay = false) }
        navigateToAfterWork()
    }

    private fun onClickWorkTimeEdit() {
        _uiState.update { it.copy(showWorkTimeEditBottomSheet = true) }
    }

    private fun onDismissMoreWorkBottomSheet() {
        _uiState.update { it.copy(showMoreWorkBottomSheet = false) }
    }

    private fun onDismissWorkTimeEditBottomSheet() {
        _uiState.update { it.copy(showWorkTimeEditBottomSheet = false) }
    }

    private fun onClickTodayVacation() {
        _uiState.update { state ->
            state.copy(
                showWorkTimeEditBottomSheet = false,
                showWorkCompletionOverlay = false,
                isOnVacation = true,
                workStatus = WorkStatus.VACATION,
            )
        }
        navigateToAfterWork()
    }

    private fun onConfirmMoreWork(intent: WorkingIntent.ConfirmMoreWork) {
        hasNavigatedToAfterWork = false
        _uiState.update { state ->
            state.copy(
                showMoreWorkBottomSheet = false,
                showWorkCompletionOverlay = false,
                endHour = intent.endHour,
                endMinute = intent.endMinute,
            )
        }
    }

    private fun onConfirmWorkTimeEdit(intent: WorkingIntent.ConfirmWorkTimeEdit) {
        _uiState.update { state ->
            state.copy(
                showWorkTimeEditBottomSheet = false,
                endHour = intent.endHour,
                endMinute = intent.endMinute,
            )
        }
        navigateToAfterWork()
    }
}