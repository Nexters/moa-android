package com.moa.app.presentation.ui.setting.companyname

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

@HiltViewModel(assistedFactory = CompanyNameViewModel.Factory::class)
class CompanyNameViewModel @AssistedInject constructor(
    @Assisted private val companyName: String,
    private val moaSideEffectBus: MoaSideEffectBus,
) : ViewModel() {
    val companyNameTextFieldState = TextFieldState(companyName)

    fun onIntent(intent: CompanyNameIntent) {
        when (intent) {
            is CompanyNameIntent.ClickBack -> back()
            is CompanyNameIntent.ClickNext -> next()
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
        fun create(companyName: String): CompanyNameViewModel
    }
}