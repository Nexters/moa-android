package com.moa.app.presentation.ui.home.afterwork

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.navigation.HomeNavigation
import com.moa.app.presentation.ui.home.afterwork.model.AfterWorkIntent
import com.moa.app.presentation.ui.home.afterwork.model.AfterWorkUiState
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

private const val DATE_CHECK_INTERVAL_MS = 60_000L

@HiltViewModel(assistedFactory = AfterWorkViewModel.Factory::class)
class AfterWorkViewModel @AssistedInject constructor(
    @Assisted private val args: HomeNavigation.AfterWork,
    private val moaSideEffectBus: MoaSideEffectBus,
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
        startDateChecker()
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
        navigateToBeforeWork()
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

        viewModelScope.launch {
            val state = _uiState.value
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
}