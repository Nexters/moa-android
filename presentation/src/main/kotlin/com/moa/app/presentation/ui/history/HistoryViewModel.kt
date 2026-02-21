package com.moa.app.presentation.ui.history

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.core.model.history.LocalDateModel
import com.moa.app.core.model.history.MonthlyWorkSummary
import com.moa.app.core.model.history.Schedule
import com.moa.app.core.model.history.ScheduleType
import com.moa.app.core.model.onboarding.Time
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.model.RootNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@Stable
data class HistoryUiState(
    val currentYear: Int = LocalDate.now().year,
    val currentMonth: Int = LocalDate.now().monthValue,
    val selectedDate: LocalDateModel = LocalDateModel(
        year = LocalDate.now().year,
        month = LocalDate.now().monthValue,
        day = LocalDate.now().dayOfMonth,
    ),
    val monthlyWorkSummary: MonthlyWorkSummary = MonthlyWorkSummary(
        currentWorkHours = 0,
        totalWorkHours = 0,
        currentSalary = 0,
        totalSalary = 0,
    ),
    val schedules: ImmutableList<Schedule> = persistentListOf(),
    val calendarDays: ImmutableList<CalendarDay> = persistentListOf(),
)

@Stable
data class CalendarDay(
    val date: LocalDateModel?,
    val isToday: Boolean = false,
    val isSelected: Boolean = false,
    val hasWorkScheduled: Boolean = false,
    val hasWorkCompleted: Boolean = false,
    val hasVacation: Boolean = false,
    val hasPayday: Boolean = false,
)

sealed interface HistoryIntent {
    data object ClickBack : HistoryIntent
    data object ClickPreviousMonth : HistoryIntent
    data object ClickNextMonth : HistoryIntent
    data object ClickAddSchedule : HistoryIntent
    data class ClickDate(val date: LocalDateModel) : HistoryIntent
    data class ClickSchedule(val schedule: Schedule) : HistoryIntent
    data class SetMonth(val year: Int, val month: Int) : HistoryIntent
}

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
) : ViewModel() {

    private val workScheduleDays = setOf(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
    )

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadMockData()
    }

    fun onIntent(intent: HistoryIntent) {
        when (intent) {
            HistoryIntent.ClickBack -> back()
            HistoryIntent.ClickPreviousMonth -> previousMonth()
            HistoryIntent.ClickNextMonth -> nextMonth()
            HistoryIntent.ClickAddSchedule -> addSchedule()
            is HistoryIntent.ClickDate -> selectDate(intent.date)
            is HistoryIntent.ClickSchedule -> editSchedule(intent.schedule)
            is HistoryIntent.SetMonth -> setMonth(intent.year, intent.month)
        }
    }

    private fun setMonth(year: Int, month: Int) {
        _uiState.update {
            it.copy(
                currentYear = year,
                currentMonth = month,
            )
        }
        updateCalendarDays()
    }

    private fun back() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(RootNavigation.Back))
        }
    }

    private fun previousMonth() {
        val current = YearMonth.of(_uiState.value.currentYear, _uiState.value.currentMonth)
        val previous = current.minusMonths(1)
        _uiState.update {
            it.copy(
                currentYear = previous.year,
                currentMonth = previous.monthValue,
            )
        }
        updateCalendarDays()
    }

    private fun nextMonth() {
        val current = YearMonth.of(_uiState.value.currentYear, _uiState.value.currentMonth)
        val next = current.plusMonths(1)
        _uiState.update {
            it.copy(
                currentYear = next.year,
                currentMonth = next.monthValue,
            )
        }
        updateCalendarDays()
    }

    private fun addSchedule() {
        viewModelScope.launch {
            moaSideEffectBus.emit(
                MoaSideEffect.Navigate(
                    RootNavigation.ScheduleForm(date = _uiState.value.selectedDate)
                )
            )
        }
    }

    private fun selectDate(date: LocalDateModel) {
        _uiState.update { it.copy(selectedDate = date) }
        updateCalendarDays()
    }

    private fun editSchedule(schedule: Schedule) {
        viewModelScope.launch {
            moaSideEffectBus.emit(
                MoaSideEffect.Navigate(
                    RootNavigation.ScheduleForm(date = schedule.date, schedule = schedule)
                )
            )
        }
    }

    private fun loadMockData() {
        val today = LocalDate.now()
        val mockSchedules = listOf(
            Schedule(
                id = 1,
                date = LocalDateModel(today.year, today.monthValue, 14),
                type = ScheduleType.WORK_SCHEDULED,
                time = Time(8, 0, 20, 0),
            ),
            Schedule(
                id = 2,
                date = LocalDateModel(today.year, today.monthValue, 15),
                type = ScheduleType.WORK_SCHEDULED,
                time = Time(8, 0, 20, 0),
            ),
            Schedule(
                id = 3,
                date = LocalDateModel(today.year, today.monthValue, 20),
                type = ScheduleType.WORK_COMPLETED,
                time = Time(9, 0, 18, 0),
            ),
            Schedule(
                id = 4,
                date = LocalDateModel(today.year, today.monthValue, 21),
                type = ScheduleType.VACATION,
            ),
            Schedule(
                id = 5,
                date = LocalDateModel(today.year, today.monthValue, 25),
                type = ScheduleType.PAYDAY,
                amount = 2000000,
            ),
            Schedule(
                id = 6,
                date = LocalDateModel(today.year, today.monthValue, 25),
                type = ScheduleType.VACATION,
            ),
        ).toImmutableList()

        _uiState.update {
            it.copy(
                schedules = mockSchedules,
                monthlyWorkSummary = MonthlyWorkSummary(
                    currentWorkHours = 120,
                    totalWorkHours = 150,
                    currentSalary = 3000,
                    totalSalary = 4000,
                ),
            )
        }
        updateCalendarDays()
    }

    private fun updateCalendarDays() {
        val year = _uiState.value.currentYear
        val month = _uiState.value.currentMonth
        val yearMonth = YearMonth.of(year, month)
        val firstDayOfMonth = yearMonth.atDay(1)
        val daysInMonth = yearMonth.lengthOfMonth()
        val startDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7

        val today = LocalDate.now()
        val selectedDate = _uiState.value.selectedDate
        val schedules = _uiState.value.schedules

        val calendarDays = mutableListOf<CalendarDay>()

        repeat(startDayOfWeek) {
            calendarDays.add(CalendarDay(date = null))
        }

        for (day in 1..daysInMonth) {
            val date = LocalDateModel(year, month, day)
            val localDate = LocalDate.of(year, month, day)
            val isFutureDate = localDate.isAfter(today)
            val isPastDate = localDate.isBefore(today)
            val isWorkDay = workScheduleDays.contains(localDate.dayOfWeek)

            val daySchedules = schedules.filter {
                it.date.year == year && it.date.month == month && it.date.day == day
            }

            val hasVacation = daySchedules.any { it.type == ScheduleType.VACATION }

            val hasWorkScheduled = if (hasVacation) {
                false
            } else {
                daySchedules.any { it.type == ScheduleType.WORK_SCHEDULED } || (isFutureDate && isWorkDay)
            }

            val hasWorkCompleted = if (hasVacation) {
                false
            } else {
                daySchedules.any { it.type == ScheduleType.WORK_COMPLETED } || (isPastDate && isWorkDay)
            }

            calendarDays.add(
                CalendarDay(
                    date = date,
                    isToday = today.year == year && today.monthValue == month && today.dayOfMonth == day,
                    isSelected = selectedDate.year == year && selectedDate.month == month && selectedDate.day == day,
                    hasWorkScheduled = hasWorkScheduled,
                    hasWorkCompleted = hasWorkCompleted,
                    hasVacation = hasVacation,
                    hasPayday = daySchedules.any { it.type == ScheduleType.PAYDAY },
                )
            )
        }

        _uiState.update { it.copy(calendarDays = calendarDays.toImmutableList()) }
    }

    fun getSchedulesForSelectedDate(): ImmutableList<Schedule> {
        val selectedDate = _uiState.value.selectedDate
        val localDate = LocalDate.of(selectedDate.year, selectedDate.month, selectedDate.day)
        val today = LocalDate.now()
        val isWorkDay = workScheduleDays.contains(localDate.dayOfWeek)

        val existingSchedules = _uiState.value.schedules.filter {
            it.date.year == selectedDate.year &&
                    it.date.month == selectedDate.month &&
                    it.date.day == selectedDate.day
        }

        val hasVacation = existingSchedules.any { it.type == ScheduleType.VACATION }
        val hasExistingWorkSchedule = existingSchedules.any {
            it.type == ScheduleType.WORK_SCHEDULED || it.type == ScheduleType.WORK_COMPLETED
        }

        val autoSchedule = if (!hasVacation && !hasExistingWorkSchedule && isWorkDay) {
            val scheduleType = if (localDate.isBefore(today)) {
                ScheduleType.WORK_COMPLETED
            } else if (localDate.isAfter(today)) {
                ScheduleType.WORK_SCHEDULED
            } else {
                null
            }

            scheduleType?.let {
                listOf(
                    Schedule(
                        id = -1,
                        date = selectedDate,
                        type = it,
                        time = Time(9, 0, 18, 0),
                    )
                )
            } ?: emptyList()
        } else {
            emptyList()
        }

        return (existingSchedules + autoSchedule).toImmutableList()
    }
}