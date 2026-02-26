package com.moa.salary.app.presentation.ui.history

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.salary.app.core.extensions.makeDateString
import com.moa.salary.app.core.model.history.LocalDateModel
import com.moa.salary.app.core.model.history.MonthlyWorkSummary
import com.moa.salary.app.core.model.history.Schedule
import com.moa.salary.app.core.model.history.ScheduleType
import com.moa.salary.app.core.model.history.Workday
import com.moa.salary.app.core.model.history.WorkdayDetail
import com.moa.salary.app.core.model.history.WorkdayType
import com.moa.salary.app.core.model.onboarding.Time
import com.moa.salary.app.data.local.PreferencesDataStore
import com.moa.salary.app.data.repository.SettingRepository
import com.moa.salary.app.data.repository.WorkdayRepository
import com.moa.salary.app.presentation.bus.MoaSideEffectBus
import com.moa.salary.app.presentation.model.MoaSideEffect
import com.moa.salary.app.presentation.model.RootNavigation
import com.moa.salary.app.presentation.model.SettingNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
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
        workedMinutes = 0,
        standardMinutes = 0,
        workedEarnings = 0,
        standardSalary = 0,
    ),
    val schedules: ImmutableList<Schedule> = persistentListOf(),
    val calendarDays: ImmutableList<CalendarDay> = persistentListOf(),
    val workdays: ImmutableList<Workday> = persistentListOf(),
    val paydayDay: Int? = null,
    val defaultWorkTime: Time = Time(9, 0, 18, 0),
    val selectedWorkdayDetail: WorkdayDetail? = null,
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
    private val workdayRepository: WorkdayRepository,
    private val preferencesDataStore: PreferencesDataStore,
    private val settingRepository: SettingRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadPaydayDay()
        loadWorkInfo()
        fetchEarnings()
        fetchWorkdays()
        fetchWorkdayDetail(_uiState.value.selectedDate)
    }

    fun refresh() {
        loadPaydayDay()
        fetchEarnings()
        fetchWorkdays()
        fetchWorkdayDetail(_uiState.value.selectedDate)
    }

    private fun loadWorkInfo() {
        viewModelScope.launch {
            try {
                val workInfo = settingRepository.getWorkInfo()
                _uiState.update { it.copy(defaultWorkTime = workInfo.workPolicy.time) }
            } catch (_: Exception) {
                // Use default time
            }
        }
    }

    private fun loadPaydayDay() {
        viewModelScope.launch {
            val paydayDay = preferencesDataStore.getPaydayDay()
            _uiState.update { it.copy(paydayDay = paydayDay) }
            updateCalendarDays()
        }
    }

    private fun fetchWorkdays() {
        viewModelScope.launch {
            try {
                val state = _uiState.value
                val workdays = workdayRepository.getWorkdays(state.currentYear, state.currentMonth)
                _uiState.update { it.copy(workdays = workdays) }
                updateCalendarDays()
            } catch (_: Exception) {
            }
        }
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
        fetchEarnings()
        fetchWorkdays()
    }

    private fun back() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.RefreshHome)
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
        fetchEarnings()
        fetchWorkdays()
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
        fetchEarnings()
        fetchWorkdays()
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
        _uiState.update { it.copy(selectedDate = date, selectedWorkdayDetail = null) }
        updateCalendarDays()
        fetchWorkdayDetail(date)
    }

    private fun fetchWorkdayDetail(date: LocalDateModel) {
        viewModelScope.launch {
            try {
                val dateString = makeDateString(date.year, date.month, date.day)
                val detail = workdayRepository.getWorkdayDetail(dateString)
                _uiState.update { it.copy(selectedWorkdayDetail = detail) }
            } catch (_: Exception) {
                _uiState.update { it.copy(selectedWorkdayDetail = null) }
            }
        }
    }

    private fun editSchedule(schedule: Schedule) {
        viewModelScope.launch {
            val destination = if (schedule.type == ScheduleType.PAYDAY) {
                val paydayDay = _uiState.value.paydayDay ?: return@launch
                SettingNavigation.SalaryDay(paydayDay)
            } else {
                RootNavigation.ScheduleForm(date = schedule.date, schedule = schedule)
            }
            moaSideEffectBus.emit(MoaSideEffect.Navigate(destination))
        }
    }

    private fun fetchEarnings() {
        viewModelScope.launch {
            try {
                val state = _uiState.value
                val earnings = workdayRepository.getEarnings(state.currentYear, state.currentMonth)
                _uiState.update { it.copy(monthlyWorkSummary = earnings) }
            } catch (_: Exception) {
            }
        }
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
        val workdays = _uiState.value.workdays
        val paydayDay = _uiState.value.paydayDay

        val calendarDays = mutableListOf<CalendarDay>()

        repeat(startDayOfWeek) {
            calendarDays.add(CalendarDay(date = null))
        }

        for (day in 1..daysInMonth) {
            val date = LocalDateModel(year, month, day)
            val localDate = LocalDate.of(year, month, day)
            val dateString = localDate.toString()
            val isFutureDate = localDate.isAfter(today)
            val isPastDate = localDate.isBefore(today)
            val isToday = localDate.isEqual(today)

            val workday = workdays.find { it.date == dateString }

            val daySchedules = schedules.filter {
                it.date.year == year && it.date.month == month && it.date.day == day
            }

            val hasVacationFromApi = workday?.type == WorkdayType.VACATION
            val hasVacation =
                hasVacationFromApi || daySchedules.any { it.type == ScheduleType.VACATION }

            val isWorkDayFromApi = workday?.type == WorkdayType.WORK

            val (hasWorkScheduled, hasWorkCompleted) = if (hasVacation) {
                false to false
            } else if (isToday && isWorkDayFromApi) {
                val currentTime = LocalTime.now()
                val defaultWorkTime = _uiState.value.defaultWorkTime
                val clockInTime =
                    LocalTime.of(defaultWorkTime.startHour, defaultWorkTime.startMinute)
                val clockOutTime = LocalTime.of(defaultWorkTime.endHour, defaultWorkTime.endMinute)

                when {
                    currentTime < clockInTime -> true to false
                    currentTime >= clockOutTime -> false to true
                    else -> false to false
                }
            } else {
                val scheduled =
                    daySchedules.any { it.type == ScheduleType.WORK_SCHEDULED } || (isFutureDate && isWorkDayFromApi)
                val completed =
                    daySchedules.any { it.type == ScheduleType.WORK_COMPLETED } || (isPastDate && isWorkDayFromApi)
                scheduled to completed
            }

            val calendarDay = CalendarDay(
                date = date,
                isToday = today.year == year && today.monthValue == month && today.dayOfMonth == day,
                isSelected = selectedDate.year == year && selectedDate.month == month && selectedDate.day == day,
                hasWorkScheduled = hasWorkScheduled,
                hasWorkCompleted = hasWorkCompleted,
                hasVacation = hasVacation,
                hasPayday = paydayDay == day,
            )

            calendarDays.add(calendarDay)
        }

        _uiState.update { it.copy(calendarDays = calendarDays.toImmutableList()) }
    }

    fun getSchedulesForSelectedDate(): ImmutableList<Schedule> {
        val state = _uiState.value
        val selectedDate = state.selectedDate
        val localDate = LocalDate.of(selectedDate.year, selectedDate.month, selectedDate.day)
        val today = LocalDate.now()
        val dateString = localDate.toString()
        val defaultWorkTime = state.defaultWorkTime
        val workdayDetail = state.selectedWorkdayDetail

        val workday = state.workdays.find { it.date == dateString }
        val detailType = workdayDetail?.type
        val isWorkDayFromApi = if (detailType != null) {
            detailType == WorkdayType.WORK
        } else {
            workday?.type == WorkdayType.WORK
        }
        val hasVacationFromApi = if (detailType != null) {
            detailType == WorkdayType.VACATION
        } else {
            workday?.type == WorkdayType.VACATION
        }

        val workTime =
            if (workdayDetail?.clockInTime != null && workdayDetail.clockOutTime != null) {
                parseTimeRange(workdayDetail.clockInTime!!, workdayDetail.clockOutTime!!)
            } else {
                defaultWorkTime
            }

        val existingSchedules = state.schedules.filter {
            it.date.year == selectedDate.year &&
                    it.date.month == selectedDate.month &&
                    it.date.day == selectedDate.day
        }

        val hasVacation =
            hasVacationFromApi || existingSchedules.any { it.type == ScheduleType.VACATION }
        val hasExistingWorkSchedule = existingSchedules.any {
            it.type == ScheduleType.WORK_SCHEDULED || it.type == ScheduleType.WORK_COMPLETED
        }

        val hasExistingVacation = existingSchedules.any { it.type == ScheduleType.VACATION }

        val autoWorkSchedule = if (!hasVacation && !hasExistingWorkSchedule && isWorkDayFromApi) {
            val isToday = localDate.isEqual(today)
            val isPastWorkEndTime = if (isToday) {
                val currentTime = LocalTime.now()
                val workEndTime = LocalTime.of(workTime.endHour, workTime.endMinute)
                currentTime >= workEndTime
            } else {
                false
            }
            val scheduleType = if (localDate.isBefore(today) || isPastWorkEndTime) {
                ScheduleType.WORK_COMPLETED
            } else {
                ScheduleType.WORK_SCHEDULED
            }

            listOf(
                Schedule(
                    id = -1,
                    date = selectedDate,
                    type = scheduleType,
                    time = workTime,
                )
            )
        } else {
            emptyList()
        }

        val autoVacationSchedule = if (hasVacationFromApi && !hasExistingVacation) {
            val vacationTime =
                if (workdayDetail?.clockInTime != null && workdayDetail.clockOutTime != null) {
                    parseTimeRange(workdayDetail.clockInTime!!, workdayDetail.clockOutTime!!)
                } else {
                    defaultWorkTime
                }
            listOf(
                Schedule(
                    id = -3,
                    date = selectedDate,
                    type = ScheduleType.VACATION,
                    time = vacationTime,
                )
            )
        } else {
            emptyList()
        }

        val paydayDay = _uiState.value.paydayDay
        val monthlyWorkSummary = state.monthlyWorkSummary
        val paydayAmount = maxOf(monthlyWorkSummary.workedEarnings, monthlyWorkSummary.standardSalary)
        val paydaySchedule = if (paydayDay == selectedDate.day) {
            listOf(
                Schedule(
                    id = -2,
                    date = selectedDate,
                    type = ScheduleType.PAYDAY,
                    amount = paydayAmount,
                )
            )
        } else {
            emptyList()
        }

        return (existingSchedules + autoWorkSchedule + autoVacationSchedule + paydaySchedule).toImmutableList()
    }

    private fun parseTimeRange(clockInTime: String, clockOutTime: String): Time {
        return try {
            val inParts = clockInTime.split(":")
            val outParts = clockOutTime.split(":")
            Time(
                startHour = inParts[0].toInt(),
                startMinute = inParts[1].toInt(),
                endHour = outParts[0].toInt(),
                endMinute = outParts[1].toInt(),
            )
        } catch (_: Exception) {
            _uiState.value.defaultWorkTime
        }
    }
}