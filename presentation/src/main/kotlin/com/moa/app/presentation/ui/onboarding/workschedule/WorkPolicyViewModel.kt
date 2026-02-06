package com.moa.app.presentation.ui.onboarding.workschedule

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.core.model.onboarding.Term
import com.moa.app.core.model.onboarding.Time
import com.moa.app.core.model.onboarding.WorkPolicy
import com.moa.app.data.repository.OnboardingRepository
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.extensions.execute
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.model.OnboardingNavigation
import com.moa.app.presentation.model.RootNavigation
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Stable
data class WorkScheduleUiState(
    val selectedWorkScheduleDays: ImmutableList<WorkPolicy.WorkScheduleDay>,
    val times: ImmutableList<Time>,
    val terms: ImmutableList<Term> = persistentListOf(),
    val showTimeBottomSheet: Time? = null,
    val showTermBottomSheet: Boolean = false,
)

@HiltViewModel(assistedFactory = WorkScheduleViewModel.Factory::class)
class WorkScheduleViewModel @AssistedInject constructor(
    @Assisted private val args: OnboardingNavigation.WorkSchedule.WorkScheduleNavigationArgs,
    private val moaSideEffectBus: MoaSideEffectBus,
    private val onboardingRepository: OnboardingRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        WorkScheduleUiState(
            selectedWorkScheduleDays = args.workScheduleDays,
            times = args.times,
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        getTerms()
    }

    fun onIntent(intent: WorkScheduleIntent) {
        when (intent) {
            WorkScheduleIntent.ClickBack -> back()
            is WorkScheduleIntent.ClickWorkScheduleDay -> clickWorkScheduleDay(intent.day)
            is WorkScheduleIntent.ShowTimeBottomSheet -> showTimeBottomSheet(intent.time)
            is WorkScheduleIntent.SetTime -> setTime(intent.time)
            is WorkScheduleIntent.ShowTermBottomSheet -> showTermBottomSheet(intent.visible)
            is WorkScheduleIntent.ClickTerm -> clickTerm(intent.term)
            is WorkScheduleIntent.ClickArrow -> clickArrow(intent.url)
            WorkScheduleIntent.ClickNext -> next()
            WorkScheduleIntent.ClickTermsNext -> putTerms()
        }
    }

    private fun getTerms() {
        suspend {
            onboardingRepository.getTerms()
        }.execute(
            bus = moaSideEffectBus,
            scope = viewModelScope,
        ){
            _uiState.value = _uiState.value.copy(terms = it)
        }
    }

    private fun back() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(RootNavigation.Back))
        }
    }

    private fun clickWorkScheduleDay(day: WorkPolicy.WorkScheduleDay) {
        val currentSet = _uiState.value.selectedWorkScheduleDays.toMutableList()
        if (currentSet.contains(day)) {
            currentSet.remove(day)
        } else {
            currentSet.add(day)
        }
        _uiState.value = _uiState.value.copy(
            selectedWorkScheduleDays = currentSet.toImmutableList(),
        )
    }

    private fun showTimeBottomSheet(time: Time?) {
        _uiState.value = _uiState.value.copy(
            showTimeBottomSheet = time,
        )
    }

    private fun setTime(time: Time) {
        val currentTimes = _uiState.value.times.toMutableList()
        val index = currentTimes.indexOfFirst { it::class == time::class }
        if (index != -1) {
            currentTimes[index] = time
            _uiState.value = _uiState.value.copy(
                times = currentTimes.toImmutableList(),
            )
        }
    }

    private fun showTermBottomSheet(visible: Boolean) {
        _uiState.value = _uiState.value.copy(
            showTermBottomSheet = visible,
        )
    }

    private fun clickTerm(term: Term) {
        val currentTerms = _uiState.value.terms.toMutableList()

        when (term) {
            is Term.All -> {
                val newChecked = !term.checked
                currentTerms.replaceAll {
                    when (it) {
                        is Term.All -> it.copy(checked = newChecked)
                        is Term.Required -> it.copy(checked = newChecked)
                        is Term.Optional -> it.copy(checked = newChecked)
                    }
                }
            }

            is Term.Required, is Term.Optional -> {
                val index = currentTerms.indexOfFirst { it == term }
                if (index != -1) {
                    val newTerm = when (term) {
                        is Term.Required -> term.copy(checked = !term.checked)
                        is Term.Optional -> term.copy(checked = !term.checked)
                        else -> term
                    }
                    currentTerms[index] = newTerm

                    val allChecked = currentTerms.drop(1).all { it.checked }
                    val allIndex = currentTerms.indexOfFirst { it is Term.All }
                    if (allIndex != -1) {
                        currentTerms[allIndex] =
                            (currentTerms[allIndex] as Term.All).copy(checked = allChecked)
                    }
                }
            }
        }

        _uiState.value = _uiState.value.copy(
            terms = currentTerms.toImmutableList(),
        )
    }

    private fun clickArrow(url: String) {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(RootNavigation.Webview(url)))
        }
    }

    private fun next() {
        if (args.isOnboarding) {
            nextIfIsOnboarding()
        } else {
            nextIfIsNotOnboarding()
        }
    }

    private fun nextIfIsOnboarding() {
        suspend {
            onboardingRepository.patchWorkPolicy(
                WorkPolicy(
                    workScheduleDays = _uiState.value.selectedWorkScheduleDays,
                    times = _uiState.value.times,
                )
            )
        }.execute(
            bus = moaSideEffectBus,
            scope = viewModelScope,
        ) {
            _uiState.value = _uiState.value.copy(showTermBottomSheet = true)
        }
    }

    private fun nextIfIsNotOnboarding() {
        // TODO setting 근무정책 api
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(OnboardingNavigation.Back))
        }
    }

    private fun putTerms() {
        suspend {
            onboardingRepository.putTerms(_uiState.value.terms)
        }.execute(
            bus = moaSideEffectBus,
            scope = viewModelScope,
        ){
            viewModelScope.launch {
                moaSideEffectBus.emit(MoaSideEffect.Navigate(OnboardingNavigation.WidgetGuide))
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(args: OnboardingNavigation.WorkSchedule.WorkScheduleNavigationArgs): WorkScheduleViewModel
    }
}