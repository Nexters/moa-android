package com.moa.salary.app.presentation.ui.onboarding.widgetguide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.salary.app.data.repository.HomeRepository
import com.moa.salary.app.presentation.bus.MoaSideEffectBus
import com.moa.salary.app.presentation.extensions.determineHomeNavigation
import com.moa.salary.app.presentation.model.MoaSideEffect
import com.moa.salary.app.presentation.model.OnboardingNavigation
import com.moa.salary.app.presentation.model.PosthogEvent
import com.moa.salary.app.presentation.model.RootNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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
            is WidgetGuideIntent.SendEvent -> sendEvent(intent.event)
        }
    }

    private fun back() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(OnboardingNavigation.Back))
        }
    }

    private fun next() {
        viewModelScope.launch {
            getHome()
        }
    }

    private fun getHome() {
        viewModelScope.launch {
            runCatching {
                homeRepository.getHome()
            }.onSuccess {
                val homeNavigation = it.determineHomeNavigation()
                navigate(RootNavigation.Home(homeNavigation))
            }.onFailure {
                toast()
            }
        }
    }

    private fun navigate(destination: RootNavigation) {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(destination))
        }
    }

    private fun toast() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Toast("일시적인 오류가 발생했어요"))
        }
    }

    private fun sendEvent(event: PosthogEvent) {
        event.sendEvent()
    }
}