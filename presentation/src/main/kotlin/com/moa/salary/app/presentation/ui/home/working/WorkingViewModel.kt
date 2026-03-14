package com.moa.salary.app.presentation.ui.home.working

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.salary.app.core.extensions.formatCurrency
import com.moa.salary.app.core.extensions.makeTimeString
import com.moa.salary.app.core.extensions.toHourMinuteSecondString
import com.moa.salary.app.core.model.work.Home
import com.moa.salary.app.core.util.Constants.TIMER_INTERVAL_MS
import com.moa.salary.app.core.util.Constants.TOOLTIP_COUNT
import com.moa.salary.app.core.util.Constants.TOOLTIP_ROTATION_INTERVAL_MS
import com.moa.salary.app.core.util.SalaryUtils
import com.moa.salary.app.data.repository.HomeRepository
import com.moa.salary.app.data.repository.WorkdayRepository
import com.moa.salary.app.presentation.bus.MoaSideEffectBus
import com.moa.salary.app.presentation.extensions.execute
import com.moa.salary.app.presentation.model.HomeNavigation
import com.moa.salary.app.presentation.model.MoaSideEffect
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Stable
data class WorkingUiState(
    val home: Home,
    val todaySalary: Long = 0L,
    val elapsedTotalSeconds: Int = 0,
    val currentTooltipIndex: Int = 0,
    val remainingHours: Int = 3,
    val showScheduleAdjustBottomSheet: Boolean = false,
    val showTimeBottomSheet: Boolean = false,
    val showMoreWorkBottomSheet: Boolean = false,
    val showWorkTimeEditBottomSheet: Boolean = false,
    val showWorkCompletionOverlay: Boolean = false,
    val showConfetti: Boolean = false,
) {
    val elapsedTimeDisplay: String
        get() = elapsedTotalSeconds.toHourMinuteSecondString()

    val startTimeDisplay: String
        get() = makeTimeString(home.startHour, home.startMinute)

    val endTimeDisplay: String
        get() = makeTimeString(home.endHour, home.endMinute)

    val progress: Float
        get() {
            if (elapsedTotalSeconds == 0) return 0f
            val startSeconds = home.startHour * 3600 + home.startMinute * 60
            val endSeconds = home.endHour * 3600 + home.endMinute * 60
            val totalSeconds = endSeconds - startSeconds

            return elapsedTotalSeconds / totalSeconds.toFloat()
        }

    val confettiProgress: Float
        get() {
            if (elapsedTotalSeconds == 0) return 0f

            val secInMinute = elapsedTotalSeconds % 1800
            return if (secInMinute == 0) 1f else (secInMinute / 1800f).coerceIn(0f, 1f)
        }

    val coinHeightFraction: Float
        get() {
            val minHeight = 0.3f
            val maxHeight = 0.7f
            return minHeight + (maxHeight - minHeight) * confettiProgress
        }

    val todaySalaryDisplay: String
        get() = formatCurrency(todaySalary)

    val monthSalaryDisplay: String
        get() = formatCurrency(home.workedEarnings)

    val totalSalaryDisplay: String
        get() = formatCurrency(home.workedEarnings + todaySalary)
}

@HiltViewModel(assistedFactory = WorkingViewModel.Factory::class)
class WorkingViewModel @AssistedInject constructor(
    @Assisted private val args: HomeNavigation.Working,
    private val moaSideEffectBus: MoaSideEffectBus,
    private val workdayRepository: WorkdayRepository,
    private val homeRepository: HomeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkingUiState(home = args.home))
    val uiState = _uiState.asStateFlow()

    init {
        startTimer()
        startTooltipRotation()
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (true) {
                delay(TIMER_INTERVAL_MS)
                checkTime()
            }
        }
    }

    private fun startTooltipRotation() {
        viewModelScope.launch {
            while (true) {
                delay(TOOLTIP_ROTATION_INTERVAL_MS)
                _uiState.update { state ->
                    val nextIndex = (state.currentTooltipIndex + 1) % TOOLTIP_COUNT
                    state.copy(currentTooltipIndex = nextIndex)
                }
            }
        }
    }

    fun onIntent(intent: WorkingIntent) {
        when (intent) {
            WorkingIntent.GetHome -> getHome()
            is WorkingIntent.ShowConfetti -> showConfetti(intent.show)
            is WorkingIntent.ShowScheduleAdjustBottomSheet -> showScheduleAdjustBottomSheet(intent.show)
            WorkingIntent.DismissTimeBottomSheet -> dismissTimeBottomSheet()
            WorkingIntent.SelectVacation -> selectVacation()
            WorkingIntent.SelectEndWork -> selectEndWork()
            WorkingIntent.SelectAdjustTime -> selectAdjustTime()

            is WorkingIntent.UpdateWorkTime -> updateWorkday(
                startHour = intent.startHour,
                startMinute = intent.startMinute,
                endHour = intent.endHour,
                endMinute = intent.endMinute,
            )

            is WorkingIntent.ShowWorkTimeEditBottomSheet -> showWorkTimeEditBottomSheet(intent.show)

            is WorkingIntent.ShowMoreWorkBottomSheet -> showMoreWorkBottomSheet(intent.show)

            WorkingIntent.ClickCompleteWork -> clickCompleteWork()

            WorkingIntent.ClickTodayVacation -> clickTodayVacation()
            is WorkingIntent.ConfirmMoreWork -> confirmMoreWork(
                endHour = intent.endHour,
                endMinute = intent.endMinute,
            )

            is WorkingIntent.ConfirmWorkTimeEdit -> confirmWorkTimeEdit(
                endHour = intent.endHour,
                endMinute = intent.endMinute,
            )
        }
    }

    private fun getHome() {
        suspend {
            homeRepository.getHome()
        }.execute(
            bus = moaSideEffectBus,
            scope = viewModelScope,
            onRetry = { getHome() },
        ) { home ->
            _uiState.update { state ->
                state.copy(home = home)
            }

            checkTime()
        }
    }

    private fun showConfetti(show: Boolean) {
        _uiState.update { it.copy(showConfetti = show) }
    }

    private fun showScheduleAdjustBottomSheet(show: Boolean) {
        _uiState.update { it.copy(showScheduleAdjustBottomSheet = show) }
    }

    private fun showMoreWorkBottomSheet(show: Boolean) {
        _uiState.update { it.copy(showMoreWorkBottomSheet = show) }
    }

    private fun showWorkTimeEditBottomSheet(show: Boolean) {
        _uiState.update { it.copy(showWorkTimeEditBottomSheet = show) }
    }

    private fun dismissTimeBottomSheet() {
        _uiState.update { it.copy(showTimeBottomSheet = false) }
    }

    private fun selectVacation() {
        val state = _uiState.value

        updateWorkday(
            startHour = state.home.startHour,
            startMinute = state.home.startMinute,
            endHour = state.home.endHour,
            endMinute = state.home.endMinute,
            type = "VACATION",
        )
    }

    private fun selectEndWork() {
        val now = LocalTime.now()

        patchClockOut(
            endHour = now.hour,
            endMinute = now.minute
        )
    }

    private fun selectAdjustTime() {
        _uiState.update { state ->
            state.copy(
                showScheduleAdjustBottomSheet = false,
                showTimeBottomSheet = true,
            )
        }
    }


    private fun clickCompleteWork() {
        navigateToAfterWork()
    }

    private fun clickTodayVacation() {
        val state = _uiState.value

        updateWorkday(
            startHour = state.home.startHour,
            startMinute = state.home.startMinute,
            endHour = state.home.endHour,
            endMinute = state.home.endMinute,
            type = "VACATION",
        )
    }

    private fun confirmMoreWork(
        endHour: Int,
        endMinute: Int,
    ) {
        patchClockOut(
            endHour = endHour,
            endMinute = endMinute,
        )
    }

    private fun confirmWorkTimeEdit(
        endHour: Int,
        endMinute: Int,
    ) {
        patchClockOut(
            endHour = endHour,
            endMinute = endMinute,
        )
    }

    private fun updateWorkday(
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int,
        type: String = "WORK",
    ) {
        val clockInTime = makeTimeString(startHour, startMinute)
        val clockOutTime = makeTimeString(endHour, endMinute)

        suspend {
            val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
            workdayRepository.updateWorkday(
                date = today,
                clockInTime = clockInTime,
                clockOutTime = clockOutTime,
                type = type
            )
        }.execute(
            scope = viewModelScope,
            bus = moaSideEffectBus,
            onRetry = { updateWorkday(startHour, startMinute, endHour, endMinute) },
        ) { workday ->
            _uiState.update {
                it.copy(
                    home = it.home.copy(
                        dailyPay = workday.dailyPay,
                        type = workday.type,
                        startHour = workday.startHour ?: it.home.startHour,
                        startMinute = workday.startMinute ?: it.home.startMinute,
                        endHour = workday.endHour ?: it.home.endHour,
                        endMinute = workday.endMinute ?: it.home.endMinute,
                    ),
                    showScheduleAdjustBottomSheet = false,
                    showTimeBottomSheet = false,
                    showMoreWorkBottomSheet = false,
                    showWorkTimeEditBottomSheet = false,
                    showWorkCompletionOverlay = false,
                )
            }

            checkTime()
        }
    }

    private fun patchClockOut(
        endHour: Int,
        endMinute: Int
    ) {
        suspend {
            val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
            val clockOutTime = makeTimeString(endHour, endMinute)
            workdayRepository.patchClockOUt(today, clockOutTime)
        }.execute(
            scope = viewModelScope,
            bus = moaSideEffectBus,
            onRetry = { patchClockOut(endHour, endMinute) }
        ) { workday ->
            _uiState.update {
                it.copy(
                    home = it.home.copy(
                        dailyPay = workday.dailyPay,
                        type = workday.type,
                        startHour = workday.startHour ?: it.home.startHour,
                        startMinute = workday.startMinute ?: it.home.startMinute,
                        endHour = workday.endHour ?: it.home.endHour,
                        endMinute = workday.endMinute ?: it.home.endMinute,
                    ),
                    showScheduleAdjustBottomSheet = false,
                    showTimeBottomSheet = false,
                    showMoreWorkBottomSheet = false,
                    showWorkTimeEditBottomSheet = false,
                    showWorkCompletionOverlay = false,
                )
            }

            checkTime()
        }
    }

    private fun checkTime() {
        val state = _uiState.value
        val now = LocalTime.now()
        val startTime = LocalTime.of(state.home.startHour, state.home.startMinute)
        val endTime = LocalTime.of(state.home.endHour, state.home.endMinute)

        when {
            now.isAfter(endTime) -> afterWork()
            now.isBefore(startTime) -> navigateToBeforeWork()
            else -> updateElapsedTime(
                startTime = startTime,
                endTime = endTime,
                now = now,
            )
        }
    }

    private fun updateElapsedTime(
        startTime: LocalTime,
        endTime: LocalTime,
        now: LocalTime,
    ) {
        val elapsedSeconds = Duration.between(startTime, now).seconds.toInt()

        _uiState.update { state ->
            val newTodaySalary = SalaryUtils.calculateSalaryForWorkedTime(
                workedSeconds = elapsedSeconds,
                startHour = state.home.startHour,
                startMinute = state.home.startMinute,
                endHour = state.home.endHour,
                endMinute = state.home.endMinute,
                dailyPay = state.home.dailyPay,
            )
            val remainingSeconds =
                Duration.between(now, endTime).seconds.toInt().coerceAtLeast(0)
            val remainingHours = remainingSeconds / 3600

            state.copy(
                elapsedTotalSeconds = elapsedSeconds,
                todaySalary = newTodaySalary,
                remainingHours = remainingHours,
            )
        }
    }

    private fun afterWork() {
        _uiState.update {
            it.copy(
                showWorkCompletionOverlay = true,
                showConfetti = true,
            )
        }
    }

    private fun navigateToBeforeWork() {
        viewModelScope.launch {
            val state = _uiState.value
            moaSideEffectBus.emit(
                MoaSideEffect.Navigate(
                    HomeNavigation.BeforeWork(home = state.home)
                )
            )
        }
    }

    private fun navigateToAfterWork() {
        viewModelScope.launch {
            val state = _uiState.value
            moaSideEffectBus.emit(
                MoaSideEffect.Navigate(
                    HomeNavigation.AfterWork(home = state.home)
                )
            )
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(args: HomeNavigation.Working): WorkingViewModel
    }
}