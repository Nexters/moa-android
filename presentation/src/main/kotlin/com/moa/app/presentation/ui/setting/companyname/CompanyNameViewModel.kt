package com.moa.app.presentation.ui.setting.companyname

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.data.repository.SettingRepository
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.extensions.execute
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
    private val settingRepository: SettingRepository,
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
        suspend {
            settingRepository.putCompanyName(companyNameTextFieldState.text.toString())
        }.execute(
            bus = moaSideEffectBus,
            scope = viewModelScope,
        ) {
            viewModelScope.launch {
                moaSideEffectBus.emit(MoaSideEffect.Navigate(SettingNavigation.Back))
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(companyName: String): CompanyNameViewModel
    }
}