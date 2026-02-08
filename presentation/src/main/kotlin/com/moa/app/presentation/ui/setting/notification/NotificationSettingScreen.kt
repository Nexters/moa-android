package com.moa.app.presentation.ui.setting.notification

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moa.app.core.model.setting.NotificationId
import com.moa.app.core.model.setting.NotificationSetting
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.component.MoaRow
import com.moa.app.presentation.designsystem.component.MoaSwitch
import com.moa.app.presentation.designsystem.component.MoaTopAppBar
import com.moa.app.presentation.designsystem.theme.MoaTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun NotificationSettingScreen(viewModel: NotificationSettingViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val isNotificationEnabled = remember {
        NotificationManagerCompat.from(context).areNotificationsEnabled()
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NotificationSettingScreen(
        isNotificationEnabled = isNotificationEnabled,
        notifications = uiState.notifications,
        onIntent = viewModel::onIntent
    )
}

@Composable
private fun NotificationSettingScreen(
    isNotificationEnabled: Boolean,
    notifications: ImmutableList<NotificationSetting>,
    onIntent: (NotificationSettingIntent) -> Unit,
) {
    Scaffold(
        topBar = {
            MoaTopAppBar(
                title = {
                    Text(
                        text = "알림 설정",
                        style = MoaTheme.typography.t3_500,
                        color = MoaTheme.colors.textHighEmphasis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onIntent(NotificationSettingIntent.ClickBack) }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_24_arrow_left),
                            contentDescription = "Back",
                            tint = MoaTheme.colors.textHighEmphasis,
                        )
                    }
                }
            )
        },
        containerColor = MoaTheme.colors.bgPrimary,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = MoaTheme.spacing.spacing20),
        ) {
            Spacer(Modifier.height(MoaTheme.spacing.spacing20))

            if (!isNotificationEnabled) {
                NotificationSettingHeaderContent()

                Spacer(Modifier.height(MoaTheme.spacing.spacing24))
            }

            NotificationSettingContent(
                isNotificationEnabled = isNotificationEnabled,
                notifications = notifications,
                onIntent = onIntent,
            )
        }
    }
}

@Composable
private fun NotificationSettingHeaderContent() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0x1FFF4037),
                shape = RoundedCornerShape(MoaTheme.radius.radius12)
            )
            .padding(
                horizontal = MoaTheme.spacing.spacing16,
                vertical = 14.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = "OS 설정에서 알림을 켜주세요.\n" +
                    "출퇴근 시간에 푸시 알림을 보내드릴게요.",
            style = MoaTheme.typography.b2_400,
            color = MoaTheme.colors.textErrorLight,
        )

        Image(
            painter = painterResource(R.drawable.ic_24_chevron_right),
            contentDescription = "Chevron Right",
        )
    }
}

@Composable
private fun NotificationSettingContent(
    isNotificationEnabled: Boolean,
    notifications: ImmutableList<NotificationSetting>,
    onIntent: (NotificationSettingIntent) -> Unit,
) {
    val serviceNotifications = notifications.filterIsInstance<NotificationSetting.Service>()
    val marketingNotifications = notifications.filterIsInstance<NotificationSetting.Marketing>()

    if (serviceNotifications.isNotEmpty()) {
        NotificationSection(
            sectionTitle = "서비스 알림",
            isNotificationEnabled = isNotificationEnabled,
            notifications = serviceNotifications,
            onIntent = onIntent,
        )
    }

    if (serviceNotifications.isNotEmpty() && marketingNotifications.isNotEmpty()) {
        Spacer(Modifier.height(MoaTheme.spacing.spacing24))
    }

    if (marketingNotifications.isNotEmpty()) {
        NotificationSection(
            sectionTitle = "광고성 정보 알림",
            isNotificationEnabled = isNotificationEnabled,
            notifications = marketingNotifications,
            onIntent = onIntent,
        )
    }
}

@Composable
private fun NotificationSection(
    sectionTitle: String,
    isNotificationEnabled: Boolean,
    notifications: List<NotificationSetting>,
    onIntent: (NotificationSettingIntent) -> Unit,
) {
    Text(
        text = sectionTitle,
        style = MoaTheme.typography.b2_500,
        color = MoaTheme.colors.textMediumEmphasis,
    )

    Spacer(Modifier.height(MoaTheme.spacing.spacing8))

    notifications.forEachIndexed { index, notification ->
        if (index > 0) {
            Spacer(Modifier.height(10.dp))
        }

        MoaRow(
            leadingContent = {
                Text(
                    text = notification.title,
                    style = MoaTheme.typography.b1_500,
                    color = if (isNotificationEnabled) {
                        MoaTheme.colors.textHighEmphasis
                    } else {
                        MoaTheme.colors.textDisabled
                    },
                )
            },
            trailingContent = {
                MoaSwitch(
                    checked = notification.enabled,
                    onCheckedChange = { enabled ->
                        onIntent(
                            NotificationSettingIntent.ToggleNotification(
                                id = notification.id,
                                enabled = enabled,
                            )
                        )
                    },
                    enabled = isNotificationEnabled,
                )
            }
        )
    }
}

sealed interface NotificationSettingIntent {
    data object ClickBack : NotificationSettingIntent
    data class ToggleNotification(
        val id: NotificationId,
        val enabled: Boolean,
    ) : NotificationSettingIntent
}

@Preview
@Composable
private fun NotificationSettingScreenPreview() {
    MoaTheme {
        NotificationSettingScreen(
            isNotificationEnabled = true,
            notifications = persistentListOf(
                NotificationSetting.Service(
                    id = NotificationId.COMMUTE,
                    title = "출퇴근 알림",
                    enabled = true,
                ),
                NotificationSetting.Service(
                    id = NotificationId.SALARY_DAY,
                    title = "월급날 알림",
                    enabled = false,
                ),
                NotificationSetting.Marketing(
                    id = NotificationId.BENEFITS,
                    title = "혜택 및 이벤트 알림",
                    enabled = true,
                ),
            ),
            onIntent = {},
        )
    }
}