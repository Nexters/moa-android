package com.moa.app.presentation.ui.onboarding.widgetguide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.data.repository.HomeRepository
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.HomeNavigation
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.model.OnboardingNavigation
import com.moa.app.presentation.model.RootNavigation
import com.moa.app.presentation.usecase.DetermineHomeNavigationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WidgetGuideViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
    private val homeRepository: HomeRepository,
    private val determineHomeNavigation: DetermineHomeNavigationUseCase,
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
            val homeNavigation = determineHomeNavigation(response)
            moaSideEffectBus.emit(MoaSideEffect.Navigate(RootNavigation.Home(homeNavigation)))
        } catch (e: Exception) {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(RootNavigation.Home(HomeNavigation.BeforeWork())))
        }
    }
}