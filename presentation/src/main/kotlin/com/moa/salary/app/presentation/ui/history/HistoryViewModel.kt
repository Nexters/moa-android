package com.moa.salary.app.presentation.ui.history

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.salary.app.core.model.calendar.Calendar
import com.moa.salary.app.core.model.calendar.Schedule
import com.moa.salary.app.data.repository.CalendarRepository
import com.moa.salary.app.presentation.bus.MoaSideEffectBus
import com.moa.salary.app.presentation.extensions.execute
import com.moa.salary.app.presentation.model.MoaSideEffect
import com.moa.salary.app.presentation.model.RootNavigation
import com.moa.salary.app.presentation.model.SettingNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@Stable
data class HistoryUiState(
    val calendar: Calendar? = null,
    val selectedYearMonth: YearMonth = YearMonth.now(),
    val selectedDate: LocalDate = LocalDate.now(),
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
    private val calendarRepository: CalendarRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState = _uiState.asStateFlow()

    fun onIntent(intent: HistoryIntent) {
        when (intent) {
            HistoryIntent.GetCalendar -> getCalendar(YearMonth.now())
            HistoryIntent.ClickBack -> clickBack()
            is HistoryIntent.SetYearMonth -> setYearMonth(intent.yearMonth)
            is HistoryIntent.ClickDate -> clickDate(intent.date)
            is HistoryIntent.ClickSchedule -> clickSchedule(intent.schedule)
            is HistoryIntent.ClickPayday -> clickPayday(intent.day)
        }
    }

    private fun getCalendar(yearMonth: YearMonth) {
        suspend {
            calendarRepository.getCalendar(
                year = yearMonth.year,
                month = yearMonth.monthValue,
            )
        }.execute(
            bus = moaSideEffectBus,
            scope = viewModelScope,
            onRetry = { getCalendar(yearMonth) },
        ) {
            _uiState.value = _uiState.value.copy(calendar = it)
        }
    }

    private fun clickBack() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(RootNavigation.Back))
        }
    }

    private fun setYearMonth(yearMonth: YearMonth) {
        _uiState.value = _uiState.value.copy(selectedYearMonth = yearMonth)
        getCalendar(yearMonth)
    }

    private fun clickDate(date: LocalDate) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
    }

    private fun clickSchedule(schedule: Schedule) {

    }

    private fun clickPayday(paydayDay: Int) {
        viewModelScope.launch {
            moaSideEffectBus.emit(
                MoaSideEffect.Navigate(
                    RootNavigation.Setting(
                        SettingNavigation.SalaryDay(paydayDay)
                    )
                )
            )
        }
    }
}