package com.moa.app.presentation.ui.onboarding.widgetguide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.core.extensions.toHourMinuteOrNull
import com.moa.app.data.remote.model.response.HomeResponse
import com.moa.app.data.remote.model.response.HomeType
import com.moa.app.data.repository.HomeRepository
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.HomeNavigation
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.model.OnboardingNavigation
import com.moa.app.presentation.model.RootNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class WidgetGuideViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
    private val homeRepository: HomeRepository,
) : ViewModel() {
    fun onIntent(intent: WidgetGuideIntent) {
        when (intent) {
            WidgetGuideIntent.ClickBack -> back()
            WidgetGuideIntent.ClickNext -> next()
        }
    }

    private fun back() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(OnboardingNavigation.Back))
        }
    }

    private fun next() {
        viewModelScope.launch {
            navigateToHome()
        }
    }

    private suspend fun navigateToHome() {
        try {
            val response = homeRepository.getHome()
            val homeNavigation = response.toHomeNavigation()
            moaSideEffectBus.emit(MoaSideEffect.Navigate(RootNavigation.Home(homeNavigation)))
        } catch (e: Exception) {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(RootNavigation.Home(HomeNavigation.BeforeWork())))
        }
    }

    private fun HomeResponse.toHomeNavigation(): HomeNavigation {
        val clockIn = clockInTime?.toHourMinuteOrNull()
        val clockOut = clockOutTime?.toHourMinuteOrNull()

        val startHour = clockIn?.first ?: 9
        val startMinute = clockIn?.second ?: 0
        val endHour = clockOut?.first ?: 18
        val endMinute = clockOut?.second ?: 0

        val isWorkDay = type != HomeType.NONE
        val isOnVacation = type == HomeType.VACATION

        val now = LocalTime.now()
        val clockInLocalTime = LocalTime.of(startHour, startMinute)
        val clockOutLocalTime = LocalTime.of(endHour, endMinute)

        val isOvernightShift = clockOutLocalTime < clockInLocalTime

        val isBeforeWork: Boolean
        val isAfterWork: Boolean

        if (isOvernightShift) {
            isBeforeWork = now >= clockOutLocalTime && now < clockInLocalTime
            isAfterWork = false
        } else {
            isBeforeWork = now < clockInLocalTime
            isAfterWork = now >= clockOutLocalTime
        }

        return when {
            isBeforeWork -> HomeNavigation.BeforeWork()
            isAfterWork -> HomeNavigation.AfterWork(
                todayEarnedSalary = dailyPay,
                startHour = startHour,
                startMinute = startMinute,
                endHour = endHour,
                endMinute = endMinute,
                isOnVacation = isOnVacation,
            )
            else -> HomeNavigation.Working(
                startHour = startHour,
                startMinute = startMinute,
                endHour = endHour,
                endMinute = endMinute,
                dailyPay = dailyPay,
                isOnVacation = isOnVacation,
                isWorkDay = isWorkDay,
            )
        }
    }
}