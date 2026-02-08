package com.moa.app.presentation.ui.setting.notification

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.app.core.model.setting.NotificationId
import com.moa.app.core.model.setting.NotificationSetting
import com.moa.app.data.repository.SettingRepository
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.extensions.execute
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.model.SettingNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
data class NotificationSettingUiState(
    val notifications: ImmutableList<NotificationSetting> = persistentListOf(),
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
            is NotificationSettingIntent.ToggleNotification -> toggleNotification(
                id = intent.id,
                enabled = intent.enabled,
            )
        }
    }

    private fun getNotificationSettings() {
        suspend {
            settingRepository.getNotificationSettings()
        }.execute(
            bus = moaSideEffectBus,
            scope = viewModelScope
        ) { notifications ->
            _uiState.value = _uiState.value.copy(notifications = notifications)
        }
    }

    private fun toggleNotification(id: NotificationId, enabled: Boolean) {
        suspend {
            settingRepository.putNotificationSetting(id, enabled)
        }.execute(
            bus = moaSideEffectBus,
            scope = viewModelScope,
        ) {
            val updatedNotifications = _uiState.value.notifications.map { notification ->
                if (notification.id == id) {
                    when (notification) {
                        is NotificationSetting.Service -> notification.copy(enabled = enabled)
                        is NotificationSetting.Marketing -> notification.copy(enabled = enabled)
                    }
                } else {
                    notification
                }
            }
            _uiState.value =
                _uiState.value.copy(notifications = updatedNotifications.toImmutableList())
        }
    }

    private fun back() {
        viewModelScope.launch {
            moaSideEffectBus.emit(MoaSideEffect.Navigate(SettingNavigation.Back))
        }
    }
}