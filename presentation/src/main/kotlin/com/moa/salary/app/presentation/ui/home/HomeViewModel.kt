package com.moa.salary.app.presentation.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.salary.app.data.repository.HomeRepository
import com.moa.salary.app.data.repository.ReviewRepository
import com.moa.salary.app.presentation.bus.MoaSideEffectBus
import com.moa.salary.app.presentation.model.MoaSideEffect
import com.moa.salary.app.presentation.model.RootNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
    private val homeRepository: HomeRepository,
    private val reviewRepository: ReviewRepository,
) : ViewModel() {
    val moaSideEffects = moaSideEffectBus.sideEffects
    var shownNotificationBottomSheet = mutableStateOf<Boolean?>(null)
    var shownPayday = mutableStateOf(true)

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.NavigateToHistory -> navigateToHistory()
            HomeIntent.NavigateToSetting -> navigateToSetting()
            HomeIntent.GetShownNotificationBottomSheet -> getShownNotificationBottomSheet()
            HomeIntent.SetShownNotificationBottomSheet -> setShownNotificationBottomSheet()
            HomeIntent.GetShownPayday -> getShownPayday()
            HomeIntent.SetShownPayday -> setShownPayday()
            HomeIntent.IncrementHomeVisit -> incrementHomeVisit()
            HomeIntent.RequestReviewOnPayday -> requestReviewOnPayday()
        }
    }

    private fun incrementHomeVisit() {
        viewModelScope.launch {
            val count = reviewRepository.incrementHomeVisitCount()
            if (count % 3 == 0) {
                moaSideEffectBus.emit(MoaSideEffect.LaunchInAppReview)
            }
        }
    }

    private fun requestReviewOnPayday() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.LaunchInAppReview)
        }
    }

    private fun navigateToHistory() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(RootNavigation.History()))
        }
    }

    private fun navigateToSetting() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(RootNavigation.Setting()))
        }
    }

    private fun getShownNotificationBottomSheet() {
        viewModelScope.launch {
            shownNotificationBottomSheet.value = homeRepository.getShownNotificationBottomSheet()
        }
    }

    private fun setShownNotificationBottomSheet() {
        shownNotificationBottomSheet.value = true
        viewModelScope.launch {
            homeRepository.putShownNotificationBottomSheet(true)
        }
    }

    private fun getShownPayday() {
        viewModelScope.launch {
            shownPayday.value = homeRepository.getShownPayday() == LocalDate.now()
        }
    }

    private fun setShownPayday() {
        shownPayday.value = true
        viewModelScope.launch {
            homeRepository.putShownPayday(LocalDate.now())
        }
    }
}