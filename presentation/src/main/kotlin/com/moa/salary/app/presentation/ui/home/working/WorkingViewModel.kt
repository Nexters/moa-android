package com.moa.salary.app.presentation.ui.home.working

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.salary.app.core.extensions.formatCurrency
import com.moa.salary.app.core.extensions.makeTimeString
import com.moa.salary.app.core.model.onboarding.Time
import com.moa.salary.app.core.model.work.Home
import com.moa.salary.app.core.util.Constants.TIMER_INTERVAL_MS
import com.moa.salary.app.core.util.Constants.TOOLTIP_COUNT
import com.moa.salary.app.core.util.Constants.TOOLTIP_ROTATION_INTERVAL_MS
import com.moa.salary.app.core.util.SalaryUtils
import com.moa.salary.app.data.repository.HomeRepository
import com.moa.salary.app.data.repository.WorkdayRepository
import com.moa.salary.app.presentation.bus.MoaSideEffectBus
import com.moa.salary.app.presentation.extensions.execute
import com.moa.salary.app.presentation.model.HistoryNavigation
import com.moa.salary.app.presentation.model.HomeNavigation
import com.moa.salary.app.presentation.model.MoaSideEffect
import com.moa.salary.app.presentation.model.RootNavigation
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

@Stable
data class WorkingUiState(
    val args: HomeNavigation.Working,
    val home: Home,
    val initialTime: Long = 0L,
    val todaySalary: Long = 0L,
    val elapsedTotalSeconds: Int = 0,
    val currentTooltipIndex: Int = 0,
    val remainingHours: Int = 3,
    val showScheduleAdjustBottomSheet: Boolean = false,
    val showMoreWorkBottomSheet: Boolean = false,
    val showWorkCompletionOverlay: Boolean,
    val showConfetti: Boolean = false,
) {
    val startTimeDisplay: String
        get() = makeTimeString(home.startHour, home.startMinute)

    val endTimeDisplay: String
        get() = makeTimeString(home.endHour, home.endMinute)

    val progress: Float
        get() {
            if (showWorkCompletionOverlay) return 1f
            if (elapsedTotalSeconds == 0) return 0f

            val startSeconds = home.startHour * 3600 + home.startMinute * 60
            val endSeconds = home.endHour * 3600 + home.endMinute * 60
            val totalSeconds = endSeconds - startSeconds

            return elapsedTotalSeconds / totalSeconds.toFloat()
        }

    val confettiProgress: Float
        get() {
            if (showWorkCompletionOverlay) return 1f
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
        get() = formatCurrency(
            if (args.showWorkCompletionOverlay) {
                home.workedEarnings
            } else {
                home.workedEarnings + todaySalary
            }
        )
}

@HiltViewModel(assistedFactory = WorkingViewModel.Factory::class)
class WorkingViewModel @AssistedInject constructor(
    @Assisted private val args: HomeNavigation.Working,
    private val moaSideEffectBus: MoaSideEffectBus,
    private val workdayRepository: WorkdayRepository,
    private val homeRepository: HomeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        WorkingUiState(
            args = args,
            home = args.home,
            showWorkCompletionOverlay = args.showWorkCompletionOverlay,
            todaySalary = if (args.showWorkCompletionOverlay) args.home.dailyPay else 0L,
        )
    )
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
            is WorkingIntent.SendEvent -> intent.event.sendEvent()
            is WorkingIntent.SetStartTime -> setStartTime(intent.time)

            WorkingIntent.GetHome -> getHome()
            is WorkingIntent.ShowConfetti -> showConfetti(intent.show)
            is WorkingIntent.ShowScheduleAdjustBottomSheet -> showScheduleAdjustBottomSheet(intent.show)
            WorkingIntent.SelectEndWork -> selectEndWork()
            is WorkingIntent.ShowMoreWorkBottomSheet -> showMoreWorkBottomSheet(intent.show)
            WorkingIntent.ClickCompleteWork -> clickCompleteWork()
            is WorkingIntent.ConfirmMoreWork -> confirmMoreWork(
                endHour = intent.endHour,
                endMinute = intent.endMinute,
            )

            WorkingIntent.NavigateToModifyWorkday -> navigateToModifyWorkday()
        }
    }

    private fun setStartTime(time: Long) {
        _uiState.value = _uiState.value.copy(initialTime = time)
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

    private fun selectEndWork() {
        val now = LocalTime.now()

        patchClockOut(
            endHour = now.hour,
            endMinute = now.minute
        )
    }

    private fun clickCompleteWork() {
        viewModelScope.launch {
            homeRepository.putCompletedWorkDay(LocalDate.now())
        }.invokeOnCompletion {
            navigateToAfterWork()
        }
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

    private fun patchClockOut(
        endHour: Int,
        endMinute: Int
    ) {
        suspend {
            val today = LocalDate.now().toString()
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
                    showMoreWorkBottomSheet = false,
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
            now.isBefore(startTime) -> navigateToBeforeWork()

            now.isBefore(endTime) -> {
                updateElapsedTime(
                    startTime = startTime,
                    endTime = endTime,
                    now = now,
                )

                _uiState.update {
                    it.copy(showWorkCompletionOverlay = false)
                }
            }

            else -> afterWork(
                startTime = startTime,
                endTime = endTime,
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

    private fun afterWork(
        startTime: LocalTime,
        endTime: LocalTime,
    ) {
        if (!_uiState.value.showWorkCompletionOverlay) {
            updateElapsedTime(
                startTime = startTime,
                endTime = endTime,
                now = endTime,
            )

            _uiState.update {
                it.copy(showWorkCompletionOverlay = true)
            }
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

    private fun navigateToModifyWorkday() {
        val home = _uiState.value.home

        viewModelScope.launch {
            moaSideEffectBus.emit(
                MoaSideEffect.Navigate(
                    RootNavigation.History(
                        startDestination = HistoryNavigation.ModifyWorkday(
                            args = HistoryNavigation.ModifyWorkday.ModifyWorkdayArgs(
                                joinedAt = null,
                                workdayType = home.type,
                                date = LocalDate.now().toString(),
                                time = Time(
                                    startHour = home.startHour,
                                    startMinute = home.startMinute,
                                    endHour = home.endHour,
                                    endMinute = home.endMinute,
                                ),
                            )
                        )
                    )
                )
            )
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(args: HomeNavigation.Working): WorkingViewModel
    }
}