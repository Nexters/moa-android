package com.moa.salary.app.presentation.ui.setting.notification

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.salary.app.core.extensions.toYearMonthDayString
import com.moa.salary.app.core.model.setting.NotificationSetting
import com.moa.salary.app.data.repository.SettingRepository
import com.moa.salary.app.presentation.bus.MoaSideEffectBus
import com.moa.salary.app.presentation.extensions.execute
import com.moa.salary.app.presentation.model.MoaSideEffect
import com.moa.salary.app.presentation.model.SettingNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@Stable
data class NotificationSettingUiState(
    val notificationSettings: ImmutableList<NotificationSetting> = persistentListOf(),
)

@HiltViewModel
class NotificationSettingViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
    private val settingRepository: SettingRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationSettingUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getNotificationSettings()
    }

    fun onIntent(intent: NotificationSettingIntent) {
        when (intent) {
            NotificationSettingIntent.ClickBack -> back()
            is NotificationSettingIntent.ToggleNotification -> toggleNotification(intent.notificationSetting)
        }
    }

    private fun getNotificationSettings() {
        suspend {
            settingRepository.getNotificationSettings()
        }.execute(
            bus = moaSideEffectBus,
            scope = viewModelScope,
            onRetry = { getNotificationSettings() },
        ) {
            _uiState.value = _uiState.value.copy(notificationSettings = it)
        }
    }

    private fun toggleNotification(notificationSetting: NotificationSetting.Content) {
        val updateNotificationSetting =
            notificationSetting.copy(checked = !notificationSetting.checked)
        suspend {
            settingRepository.patchNotificationSetting(updateNotificationSetting)
        }.execute(
            bus = moaSideEffectBus,
            scope = viewModelScope,
            onRetry = { toggleNotification(notificationSetting) }
        ) {
            sendNotificationToast(updateNotificationSetting)

            val updatedNotificationSettings = _uiState.value.notificationSettings.map {
                if (it is NotificationSetting.Content && it.type == updateNotificationSetting.type) {
                    updateNotificationSetting
                } else {
                    it
                }
            }
            _uiState.value =
                _uiState.value.copy(notificationSettings = updatedNotificationSettings.toImmutableList())
        }
    }

    private fun sendNotificationToast(notificationSetting: NotificationSetting.Content) {
        val toastMessage = buildString {
            append(LocalDate.now().toYearMonthDayString())
            append(" ")

            if (notificationSetting.type == "MARKETING") {
                val status = if (notificationSetting.checked) "수신 동의 완료" else "수신 동의 철회"
                append("마케팅 정보 $status\n")
            }

            val status = if (notificationSetting.checked) "을 켰어요." else "을 껐어요."
            append("${notificationSetting.title}$status")
        }

        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Toast(toastMessage))
        }
    }

    private fun back() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(SettingNavigation.Back))
        }
    }
}