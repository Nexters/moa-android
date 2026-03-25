package com.moa.salary.app.presentation.ui.history.scheduleform

//import androidx.compose.runtime.Stable
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.moa.salary.app.core.extensions.makeDateString
//import com.moa.salary.app.core.extensions.makeTimeString
//import com.moa.salary.app.core.model.work.LocalDateModel
//import com.moa.salary.app.core.model.work.Schedule
//import com.moa.salary.app.core.model.work.WorkdayType
//import com.moa.salary.app.core.model.onboarding.Time
//import com.moa.salary.app.data.repository.SettingRepository
//import com.moa.salary.app.data.repository.WorkdayRepository
//import com.moa.salary.app.presentation.bus.MoaSideEffectBus
//import com.moa.salary.app.presentation.model.MoaSideEffect
//import com.moa.salary.app.presentation.model.RootNavigation
//import dagger.assisted.Assisted
//import dagger.assisted.AssistedFactory
//import dagger.assisted.AssistedInject
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//
//enum class ScheduleInputType {
//    VACATION,
//    WORK,
//}
//
//@Stable
//data class ScheduleFormUiState(
//    val isEditMode: Boolean = false,
//    val scheduleId: Long = 0,
//    val date: LocalDateModel = LocalDateModel.today(),
//    val isDateSelected: Boolean = false,
//    val scheduleType: ScheduleInputType = ScheduleInputType.WORK,
//    val time: Time = Time(9, 0, 18, 0),
//    val showDateBottomSheet: Boolean = false,
//    val showTimeBottomSheet: Boolean = false,
//)
//
//@HiltViewModel(assistedFactory = ScheduleFormViewModel.Factory::class)
//class ScheduleFormViewModel @AssistedInject constructor(
//    @Assisted private val initialDate: LocalDateModel,
//    @Assisted private val schedule: Schedule?,
//    private val moaSideEffectBus: MoaSideEffectBus,
//    private val workdayRepository: WorkdayRepository,
//    private val settingRepository: SettingRepository,
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow(
//        if (schedule != null) {
//            ScheduleFormUiState(
//                isEditMode = true,
//                scheduleId = schedule.id,
//                date = schedule.date,
//                isDateSelected = true,
//                scheduleType = when (schedule.type) {
//                    ScheduleType.VACATION -> ScheduleInputType.VACATION
//                    else -> ScheduleInputType.WORK
//                },
//                time = schedule.time ?: Time(9, 0, 18, 0),
//            )
//        } else {
//            ScheduleFormUiState(
//                isEditMode = false,
//                date = initialDate,
//                isDateSelected = false,
//            )
//        }
//    )
//    val uiState = _uiState.asStateFlow()
//
//    init {
//        loadDefaultWorkTime()
//    }
//
//    private fun loadDefaultWorkTime() {
//        viewModelScope.launch {
//            try {
//                val workInfo = settingRepository.getWorkInfo()
//                val defaultTime = workInfo.workPolicy.time
//                _uiState.update { state ->
//                    if (state.time == Time(9, 0, 18, 0)) {
//                        state.copy(time = defaultTime)
//                    } else {
//                        state
//                    }
//                }
//            } catch (_: Exception) {
//                // Use default time
//            }
//        }
//    }
//
//    fun onIntent(intent: ScheduleFormIntent) {
//        when (intent) {
//            ScheduleFormIntent.ClickBack -> back()
//            is ScheduleFormIntent.SelectScheduleType -> selectType(intent.type)
//            is ScheduleFormIntent.SetDate -> setDate(intent.date)
//            is ScheduleFormIntent.SetTime -> setTime(intent.time)
//            is ScheduleFormIntent.ShowDateBottomSheet -> showDateBottomSheet(intent.visible)
//            is ScheduleFormIntent.ShowTimeBottomSheet -> showTimeBottomSheet(intent.visible)
//            ScheduleFormIntent.ClickCancel -> cancel()
//            ScheduleFormIntent.ClickConfirm -> confirm()
//        }
//    }
//
//    private fun back() {
//        viewModelScope.launch {
//            moaSideEffectBus.emit(MoaSideEffect.Navigate(RootNavigation.Back))
//        }
//    }
//
//    private fun selectType(type: ScheduleInputType) {
//        _uiState.update { it.copy(scheduleType = type) }
//    }
//
//    private fun setDate(date: LocalDateModel) {
//        _uiState.update { it.copy(date = date, isDateSelected = true, showDateBottomSheet = false) }
//    }
//
//    private fun setTime(time: Time) {
//        _uiState.update { it.copy(time = time, showTimeBottomSheet = false) }
//    }
//
//    private fun showDateBottomSheet(visible: Boolean) {
//        _uiState.update { it.copy(showDateBottomSheet = visible) }
//    }
//
//    private fun showTimeBottomSheet(visible: Boolean) {
//        _uiState.update { it.copy(showTimeBottomSheet = visible) }
//    }
//
//    private fun cancel() {
//        viewModelScope.launch {
//            moaSideEffectBus.emit(MoaSideEffect.Navigate(RootNavigation.Back))
//        }
//    }
//
//    private fun confirm() {
//        viewModelScope.launch {
//            val state = _uiState.value
//            if (!state.isDateSelected) return@launch
//            val selectedDate = state.date
//            val date = makeDateString(selectedDate.year, selectedDate.month, selectedDate.day)
//            val workdayType = when (state.scheduleType) {
//                ScheduleInputType.WORK -> WorkdayType.WORK
//                ScheduleInputType.VACATION -> WorkdayType.VACATION
//            }
//            val clockInTime = makeTimeString(state.time.startHour, state.time.startMinute)
//            val clockOutTime = makeTimeString(state.time.endHour, state.time.endMinute)
//
//            try {
//                moaSideEffectBus.emit(MoaSideEffect.Loading(true))
//                workdayRepository.updateWorkday(
//                    date = date,
//                    type = workdayType,
//                    clockInTime = clockInTime,
//                    clockOutTime = clockOutTime,
//                )
//                moaSideEffectBus.emit(MoaSideEffect.Loading(false))
//                moaSideEffectBus.emit(MoaSideEffect.Navigate(RootNavigation.Back))
//            } catch (e: Exception) {
//                moaSideEffectBus.emit(MoaSideEffect.Loading(false))
//                moaSideEffectBus.emit(MoaSideEffect.Failure(e) { confirm() })
//            }
//        }
//    }
//
//    @AssistedFactory
//    interface Factory {
//        fun create(initialDate: LocalDateModel, schedule: Schedule?): ScheduleFormViewModel
//    }
//}