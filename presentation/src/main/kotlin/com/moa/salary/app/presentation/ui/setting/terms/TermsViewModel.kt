package com.moa.salary.app.presentation.ui.setting.terms

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.salary.app.core.model.setting.SettingTerm
import com.moa.salary.app.data.repository.SettingRepository
import com.moa.salary.app.presentation.bus.MoaSideEffectBus
import com.moa.salary.app.presentation.extensions.execute
import com.moa.salary.app.presentation.model.MoaSideEffect
import com.moa.salary.app.presentation.model.RootNavigation
import com.moa.salary.app.presentation.model.SettingNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
data class TermsUiState(
    val terms: ImmutableList<SettingTerm> = persistentListOf()
)

@HiltViewModel
class TermsViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
    private val settingRepository: SettingRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TermsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getTerms()
    }

    fun onIntent(intent: TermsIntent) {
        when (intent) {
            is TermsIntent.ClickBack -> back()
            is TermsIntent.ClickTerm -> term(intent.term)
        }
    }

    private fun getTerms() {
        suspend {
            settingRepository.getTerms()
        }.execute(
            bus = moaSideEffectBus,
            scope = viewModelScope,
            onRetry = { getTerms() }
        ) {
            _uiState.value = _uiState.value.copy(terms = it)
        }
    }

    private fun back() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(SettingNavigation.Back))
        }
    }

    private fun term(term: SettingTerm) {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(RootNavigation.Webview(term.url)))
        }
    }
}