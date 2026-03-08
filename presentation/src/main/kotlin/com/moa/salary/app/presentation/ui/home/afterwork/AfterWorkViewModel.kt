package com.moa.salary.app.presentation.ui.home.afterwork

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.salary.app.core.extensions.formatCurrency
import com.moa.salary.app.core.extensions.makeTimeString
import com.moa.salary.app.core.model.work.Home
import com.moa.salary.app.core.util.Constants.TIME_CHECK_INTERVAL_MS
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
import java.time.format.DateTimeFormatter
import java.util.Locale

@Stable
data class AfterWorkUiState(
    val today: LocalDate = LocalDate.now(),
    val home: Home,
    val showMoreWorkBottomSheet: Boolean = false,
    val showConfetti: Boolean = true,
) {
    val accumulatedSalary: String
        get() = formatCurrency(home.workedEarnings)

    val todaySalary: String
        get() = "+${formatCurrency(home.dailyPay)}원"

    val dateDisplay: String
        get() = today.format(DateTimeFormatter.ofPattern("M월 d일 (E)", Locale.KOREAN))

    val month: Int
        get() = today.monthValue

    val workTimeDisplay: String
        get() = "${makeTimeString(home.startHour, home.startMinute)} - ${
            makeTimeString(
                home.endHour,
                home.endMinute
            )
        }"
}

@HiltViewModel(assistedFactory = AfterWorkViewModel.Factory::class)
class AfterWorkViewModel @AssistedInject constructor(
    @Assisted private val args: HomeNavigation.AfterWork,
    private val moaSideEffectBus: MoaSideEffectBus,
    private val workdayRepository: WorkdayRepository,
    private val homeRepository: HomeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AfterWorkUiState(home = args.home))
    val uiState: StateFlow<AfterWorkUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            while (true) {
                delay(TIME_CHECK_INTERVAL_MS)
                checkTime()
            }
        }
    }

    fun onIntent(intent: AfterWorkIntent) {
        when (intent) {
            AfterWorkIntent.GetHome -> getHome()
            AfterWorkIntent.ClickHistory -> clickHistory()
            AfterWorkIntent.ClickMoreWork -> clickMoreWork()
            AfterWorkIntent.DismissMoreWorkBottomSheet -> dismissMoreWorkBottomSheet()
            AfterWorkIntent.DismissConfetti -> dismissConfetti()
            is AfterWorkIntent.ConfirmMoreWork -> confirmMoreWork(
                endHour = intent.endHour,
                endMinute = intent.endMinute
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

    private fun clickHistory() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(RootNavigation.History))
        }
    }

    private fun clickMoreWork() {
        _uiState.update { it.copy(showMoreWorkBottomSheet = true) }
    }

    private fun dismissMoreWorkBottomSheet() {
        _uiState.update { it.copy(showMoreWorkBottomSheet = false) }
    }

    private fun dismissConfetti() {
        _uiState.update { it.copy(showConfetti = false) }
    }

    private fun confirmMoreWork(
        endHour: Int,
        endMinute: Int,
    ) {
        _uiState.update { it.copy(showMoreWorkBottomSheet = false) }

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
            is HomeNavigation.BeforeWork -> navigate(homeNavigation)
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
        fun create(args: HomeNavigation.AfterWork): AfterWorkViewModel
    }
}