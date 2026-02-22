package com.moa.app.presentation.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.data.remote.model.response.HomeResponse
import com.moa.app.data.remote.model.response.HomeType
import com.moa.app.data.repository.HomeRepository
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.HomeNavigation
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.model.RootNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
    private val homeRepository: HomeRepository,
) : ViewModel() {
    val moaSideEffects = moaSideEffectBus.sideEffects

    private val _initialNavigation = MutableStateFlow<HomeNavigation?>(null)
    val initialNavigation: StateFlow<HomeNavigation?> = _initialNavigation.asStateFlow()

    init {
        loadInitialNavigation()
    }

    private fun loadInitialNavigation() {
        viewModelScope.launch {
            try {
                val response = homeRepository.getHome()
                Log.d(TAG, "[Home] API response: type=${response.type}, clockIn=${response.clockInTime}, clockOut=${response.clockOutTime}")
                val navigation = response.toInitialNavigation()
                Log.d(TAG, "[Home] Initial navigation: $navigation")
                _initialNavigation.value = navigation
            } catch (e: Exception) {
                Log.e(TAG, "[Home] Failed to load home data, defaulting to BeforeWork", e)
                _initialNavigation.value = HomeNavigation.BeforeWork()
            }
        }
    }

    private fun HomeResponse.toInitialNavigation(): HomeNavigation {
        val clockIn = clockInTime?.let { parseTime(it) }
        val clockOut = clockOutTime?.let { parseTime(it) }

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

        Log.d(TAG, "[Home] Time check: now=$now, clockIn=$clockInLocalTime, clockOut=$clockOutLocalTime, isOvernightShift=$isOvernightShift")

        val isBeforeWork: Boolean
        val isAfterWork: Boolean

        if (isOvernightShift) {
            isBeforeWork = now >= clockOutLocalTime && now < clockInLocalTime
            isAfterWork = false
        } else {
            isBeforeWork = now < clockInLocalTime
            isAfterWork = now >= clockOutLocalTime
        }

        Log.d(TAG, "[Home] Conditions: isBeforeWork=$isBeforeWork, isAfterWork=$isAfterWork")

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

    private fun parseTime(time: String): Pair<Int, Int>? {
        return try {
            val parts = time.split(":")
            if (parts.size >= 2) {
                Pair(parts[0].toInt(), parts[1].toInt())
            } else null
        } catch (e: Exception) {
            null
        }
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.NavigateToHistory -> navigateToHistory()
            is HomeIntent.NavigateToSetting -> navigateToSetting()
        }
    }

    private fun navigateToHistory() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(RootNavigation.History))
        }
    }

    private fun navigateToSetting() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(RootNavigation.Setting))
        }
    }

    companion object {
        private const val TAG = "HomeNavigation"
    }
}