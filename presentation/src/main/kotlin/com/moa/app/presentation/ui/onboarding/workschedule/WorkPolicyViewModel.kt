package com.moa.app.presentation.ui.onboarding.workschedule

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.core.model.onboarding.Term
import com.moa.app.core.model.onboarding.Time
import com.moa.app.core.model.onboarding.WorkPolicy
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.navigation.OnboardingNavigation
import com.moa.app.presentation.navigation.RootNavigation
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
    val selectedWorkScheduleDays: ImmutableList<WorkPolicy.WorkScheduleDay> = persistentListOf(),
    val times: ImmutableList<Time> = persistentListOf(
        Time.Work(
            startHour = 9,
            startMinute = 0,
            endHour = 18,
            endMinute = 0,
        ),
        Time.Lunch(
            startHour = 12,
            startMinute = 0,
            endHour = 13,
            endMinute = 0,
        )
    ),
    val terms: ImmutableList<Term> = persistentListOf(
        Term.All(
            title = "전체 동의하기",
            url = "",
            checked = false,
        ),
        Term.Required(
            title = "(필수) 서비스 이용 약관 동의",
            url = "https://www.naver.com",
            checked = false,
        ),
        Term.Required(
            title = "(필수) 테스트 이용 약관 동의",
            url = "https://www.naver.com",
            checked = false,
        ),
        Term.Optional(
            title = "(선택) 서비스 이용 약관 동의",
            url = "https://www.naver.com",
            checked = false,
        ),
        Term.Optional(
            title = "(선택) 테스트 이용 약관 동의",
            url = "https://www.naver.com",
            checked = false,
        ),
    ),
    val showTimeBottomSheet: Time? = null,
    val showTermBottomSheet: Boolean = false,
)

@HiltViewModel(assistedFactory = WorkScheduleViewModel.Factory::class)
class WorkScheduleViewModel @AssistedInject constructor(
    @Assisted private val args: OnboardingNavigation.WorkSchedule.WorkScheduleNavigationArgs,
    private val moaSideEffectBus: MoaSideEffectBus,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        WorkScheduleUiState(
            selectedWorkScheduleDays = args.workScheduleDays,
            times = args.times,
        )
    )
    val uiState = _uiState.asStateFlow()

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
        viewModelScope.launch {
            // TODO args 서버로 넘기고 이동시키기
            moaSideEffectBus.emit(
                MoaSideEffect.Navigate(
                    destination = if (args.isOnboarding) {
                        OnboardingNavigation.WidgetGuide
                    } else {
                        RootNavigation.Back
                    }
                )
            )
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(args: OnboardingNavigation.WorkSchedule.WorkScheduleNavigationArgs): WorkScheduleViewModel
    }
}