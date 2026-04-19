package com.moa.salary.app.presentation.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.salary.app.data.repository.HomeRepository
import com.moa.salary.app.presentation.bus.MoaSideEffectBus
import com.moa.salary.app.presentation.model.MoaSideEffect
import com.moa.salary.app.presentation.model.RootNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
    private val homeRepository: HomeRepository,
) : ViewModel() {
    val moaSideEffects = moaSideEffectBus.sideEffects
    var shownNotificationBottomSheet = mutableStateOf<Boolean?>(null)

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.NavigateToHistory -> navigateToHistory()
            is HomeIntent.NavigateToSetting -> navigateToSetting()
            is HomeIntent.GetShownNotificationBottomSheet -> getShownNotificationBottomSheet()
            is HomeIntent.SetShownNotificationBottomSheet -> setShownNotificationBottomSheet()
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
}