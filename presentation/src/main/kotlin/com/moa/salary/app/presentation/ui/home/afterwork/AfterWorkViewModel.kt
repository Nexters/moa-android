package com.moa.salary.app.presentation.ui.home.afterwork

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.salary.app.core.extensions.makeTimeString
import com.moa.salary.app.data.repository.HomeRepository
import com.moa.salary.app.data.repository.WorkdayRepository
import com.moa.salary.app.presentation.bus.MoaSideEffectBus
import com.moa.salary.app.presentation.extensions.execute
import com.moa.salary.app.presentation.model.HomeNavigation
import com.moa.salary.app.presentation.model.MoaSideEffect
import com.moa.salary.app.presentation.model.RootNavigation
import com.moa.salary.app.presentation.ui.home.afterwork.model.AfterWorkIntent
import com.moa.salary.app.presentation.ui.home.afterwork.model.AfterWorkUiState
import com.moa.salary.app.presentation.ui.widget.util.WidgetUpdateManager
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

private const val DATE_CHECK_INTERVAL_MS = 60_000L

@HiltViewModel(assistedFactory = AfterWorkViewModel.Factory::class)
class AfterWorkViewModel @AssistedInject constructor(
    @Assisted private val args: HomeNavigation.AfterWork,
    private val moaSideEffectBus: MoaSideEffectBus,
    val widgetUpdateManager: WidgetUpdateManager,
    private val workdayRepository: WorkdayRepository,
    private val homeRepository: HomeRepository,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(args: HomeNavigation.AfterWork): AfterWorkViewModel
    }

    private val entryDate: LocalDate = LocalDate.now()

    private val _uiState = MutableStateFlow(
        AfterWorkUiState(
            month = LocalDate.now().monthValue,
            todaySalary = args.todayEarnedSalary,
            startHour = args.startHour,
            startMinute = args.startMinute,
            endHour = args.endHour,
            endMinute = args.endMinute,
            isOnVacation = args.isOnVacation,
        )
    )
    val uiState: StateFlow<AfterWorkUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
        startDateChecker()
        observeRefreshHome()
    }

    private fun observeRefreshHome() {
        viewModelScope.launch {
            moaSideEffectBus.sideEffects.collect { effect ->
                if (effect is MoaSideEffect.RefreshHome) {
                    loadHomeData()
                }
            }
        }
    }

    private fun loadHomeData() {
        suspend {
            homeRepository.getHome()
        }.execute(scope = viewModelScope) { homeResponse ->
            _uiState.update { state ->
                state.copy(
                    location = homeResponse.workplace,
                    workedEarnings = homeResponse.workedEarnings,
                    standardSalary = homeResponse.standardSalary,
                    dailyPay = homeResponse.dailyPay,
                )
            }
        }
    }

    private fun startDateChecker() {
        viewModelScope.launch {
            while (true) {
                delay(DATE_CHECK_INTERVAL_MS)
                checkDateChanged()
            }
        }
    }

    private fun checkDateChanged() {
        val currentDate = LocalDate.now()
        if (currentDate != entryDate) {
            navigateToBeforeWork()
        }
    }

    private fun navigateToBeforeWork() {
        viewModelScope.launch {
            val state = _uiState.value
            moaSideEffectBus.emit(
                MoaSideEffect.Navigate(
                    HomeNavigation.BeforeWork(
                        todayEarnedSalary = state.todaySalary,
                    )
                )
            )
        }
    }

    fun onIntent(intent: AfterWorkIntent) {
        when (intent) {
            AfterWorkIntent.ClickCheckWorkHistory -> onClickCheckWorkHistory()
            AfterWorkIntent.ClickMoreWork -> onClickMoreWork()
            AfterWorkIntent.ClickComplete -> onClickComplete()
            AfterWorkIntent.DismissMoreWorkBottomSheet -> onDismissMoreWorkBottomSheet()
            AfterWorkIntent.DismissConfetti -> onDismissConfetti()
            is AfterWorkIntent.ConfirmMoreWork -> onConfirmMoreWork(intent)
        }
    }

    private fun onDismissConfetti() {
        _uiState.update { it.copy(showConfetti = false) }
    }

    private fun onClickCheckWorkHistory() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(RootNavigation.History))
        }
    }

    private fun onClickMoreWork() {
        _uiState.update { it.copy(showMoreWorkBottomSheet = true) }
    }

    private fun onClickComplete() {
        navigateToBeforeWork()
    }

    private fun onDismissMoreWorkBottomSheet() {
        _uiState.update { it.copy(showMoreWorkBottomSheet = false) }
    }

    private fun onConfirmMoreWork(intent: AfterWorkIntent.ConfirmMoreWork) {
        _uiState.update { it.copy(showMoreWorkBottomSheet = false) }

        val state = _uiState.value
        updateClockOutTimeApi(intent.endHour, intent.endMinute)

        viewModelScope.launch {
            moaSideEffectBus.emit(
                MoaSideEffect.Navigate(
                    HomeNavigation.Working(
                        startHour = state.startHour,
                        startMinute = state.startMinute,
                        endHour = intent.endHour,
                        endMinute = intent.endMinute,
                    )
                )
            )
        }
    }

    private fun updateClockOutTimeApi(endHour: Int, endMinute: Int) {
        suspend {
            val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
            val clockOutTime = makeTimeString(endHour, endMinute)
            workdayRepository.updateClockOutTime(today, clockOutTime)
        }.execute(scope = viewModelScope) {}
    }
}