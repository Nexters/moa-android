package com.moa.salary.app.presentation.ui.home.beforework

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.salary.app.core.extensions.formatCurrency
import com.moa.salary.app.core.extensions.makeTimeString
import com.moa.salary.app.core.model.work.Home
import com.moa.salary.app.core.model.work.WorkdayType
import com.moa.salary.app.core.util.Constants.TIMER_INTERVAL_MS
import com.moa.salary.app.data.repository.HomeRepository
import com.moa.salary.app.data.repository.WorkdayRepository
import com.moa.salary.app.presentation.bus.MoaSideEffectBus
import com.moa.salary.app.presentation.extensions.determineHomeNavigation
import com.moa.salary.app.presentation.extensions.execute
import com.moa.salary.app.presentation.model.HomeNavigation
import com.moa.salary.app.presentation.model.MoaSideEffect
import com.moa.salary.app.presentation.model.RootNavigation
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
import java.util.Locale

@Stable
data class BeforeWorkUiState(
    val today: LocalDate = LocalDate.now(),
    val home: Home,
    val showTimeBottomSheet: Boolean = false,
) {
    val accumulatedSalary: String
        get() = formatCurrency(home.workedEarnings)

    val todaySalary: String
        get() = "${formatCurrency(home.dailyPay)}원"

    val dateDisplay: String
        get() = today.format(DateTimeFormatter.ofPattern("M월 d일 (E)", Locale.KOREAN))

    val month: Int
        get() = today.monthValue

    val workTimeDisplay: String
        get() = if (home.type == WorkdayType.VACATION) {
            "휴가"
        } else {
            "${makeTimeString(home.startHour, home.startMinute)} - ${
                makeTimeString(
                    home.endHour,
                    home.endMinute
                )
            }"
        }

    val autoClockInTime: String
        get() = makeTimeString(home.startHour, home.startMinute)

    val additionalSalary: Long?
        get() = if (home.workedEarnings > home.standardSalary) home.workedEarnings - home.standardSalary else null

    val additionalSalaryDisplay: String?
        get() = additionalSalary?.let { String.format(Locale.getDefault(), "+%,d원", it) }
}

@HiltViewModel(assistedFactory = BeforeWorkViewModel.Factory::class)
class BeforeWorkViewModel @AssistedInject constructor(
    @Assisted private val args: HomeNavigation.BeforeWork,
    private val moaSideEffectBus: MoaSideEffectBus,
    private val homeRepository: HomeRepository,
    private val workdayRepository: WorkdayRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(BeforeWorkUiState(home = args.home))
    val uiState: StateFlow<BeforeWorkUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            while (true) {
                delay(TIMER_INTERVAL_MS)
                checkTime()
            }
        }
    }

    fun onIntent(intent: BeforeWorkIntent) {
        when (intent) {
            BeforeWorkIntent.GetHome -> getHome()
            BeforeWorkIntent.ClickWorkTime -> clockWorkTime()
            BeforeWorkIntent.ClickEarlyClockIn -> clickEarlyClockIn()
            BeforeWorkIntent.ClickVacation -> clickVacation()
            BeforeWorkIntent.ClickClockInOnDayOff -> clickClockInOnDayOff()
            BeforeWorkIntent.DismissTimeBottomSheet -> dismissTimeBottomSheet()
            is BeforeWorkIntent.UpdateWorkTime -> updateWorkday(
                startHour = intent.startHour,
                startMinute = intent.startMinute,
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

    private fun clockWorkTime() {
        _uiState.update { it.copy(showTimeBottomSheet = true) }
    }

    private fun dismissTimeBottomSheet() {
        _uiState.update { it.copy(showTimeBottomSheet = false) }
    }

    private fun clickEarlyClockIn() {
        val now = LocalTime.now()
        val state = _uiState.value

        val registeredStartMinutes = state.home.startHour * 60 + state.home.startMinute
        val registeredEndMinutes = state.home.endHour * 60 + state.home.endMinute
        val workDurationMinutes = registeredEndMinutes - registeredStartMinutes
        val endTime = now.plusMinutes(workDurationMinutes.toLong())

        updateWorkday(
            startHour = now.hour,
            startMinute = now.minute,
            endHour = endTime.hour,
            endMinute = endTime.minute,
        )
    }

    private fun clickVacation() {
        val state = _uiState.value
        val now = LocalTime.now()

        val registeredStartMinutes = state.home.startHour * 60 + state.home.startMinute
        val registeredEndMinutes = state.home.endHour * 60 + state.home.endMinute
        val workDurationMinutes = registeredEndMinutes - registeredStartMinutes
        val endTime = now.plusMinutes(workDurationMinutes.toLong())

        updateWorkday(
            startHour = now.hour,
            startMinute = now.minute,
            endHour = endTime.hour,
            endMinute = endTime.minute,
            type = "VACATION",
        )
    }

    private fun clickClockInOnDayOff() {
        val now = LocalTime.now()
        val endTime = now.plusHours(3)

        updateWorkday(
            startHour = now.hour,
            startMinute = now.minute,
            endHour = endTime.hour,
            endMinute = endTime.minute,
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
                    )
                )
            }

            checkTime()
        }
    }

    private fun checkTime() {
        val state = _uiState.value

        val homeNavigation = state.home.determineHomeNavigation()

        when (homeNavigation) {
            is HomeNavigation.Working -> navigate(homeNavigation)
            is HomeNavigation.AfterWork -> navigate(homeNavigation)
            else -> Unit
        }
    }

    private fun navigate(navigation: RootNavigation) {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(navigation))
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(args: HomeNavigation.BeforeWork): BeforeWorkViewModel
    }
}