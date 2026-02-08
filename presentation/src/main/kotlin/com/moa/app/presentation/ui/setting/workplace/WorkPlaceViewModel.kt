package com.moa.app.presentation.ui.setting.workplace

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.model.SettingNavigation
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = WorkPlaceViewModel.Factory::class)
class WorkPlaceViewModel @AssistedInject constructor(
    @Assisted private val workPlace : String,
    private val moaSideEffectBus: MoaSideEffectBus,
) : ViewModel() {
    val workPlaceTextFieldState = TextFieldState(workPlace)

    fun onIntent(intent: WorkPlaceIntent) {
        when (intent) {
            is WorkPlaceIntent.ClickBack -> back()
            is WorkPlaceIntent.ClickNext -> next()
        }
    }

    private fun back() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(SettingNavigation.Back))
        }
    }

    private fun next() {
        // TODO setting 근무지 api
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(SettingNavigation.Back))
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(workPlace : String): WorkPlaceViewModel
    }
}