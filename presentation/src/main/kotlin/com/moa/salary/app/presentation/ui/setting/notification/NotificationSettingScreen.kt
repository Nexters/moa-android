package com.moa.salary.app.presentation.ui.setting.notification

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moa.salary.app.core.model.setting.NotificationSetting
import com.moa.salary.app.presentation.R
import com.moa.salary.app.presentation.designsystem.component.MoaRow
import com.moa.salary.app.presentation.designsystem.component.MoaSwitch
import com.moa.salary.app.presentation.designsystem.component.MoaTopAppBar
import com.moa.salary.app.presentation.designsystem.theme.MoaTheme
import com.moa.salary.app.presentation.extensions.openNotificationSettings
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
        notificationSettings = uiState.notificationSettings,
        onIntent = viewModel::onIntent
    )
}

@Composable
private fun NotificationSettingScreen(
    isNotificationEnabled: Boolean,
    notificationSettings: ImmutableList<NotificationSetting>,
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
            if (!isNotificationEnabled) {
                Spacer(Modifier.height(MoaTheme.spacing.spacing24))

                NotificationSettingHeaderContent()
            }

            NotificationSettingContent(
                isNotificationEnabled = isNotificationEnabled,
                notificationSettings = notificationSettings,
                onIntent = onIntent,
            )
        }
    }
}

@Composable
private fun NotificationSettingHeaderContent() {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(MoaTheme.radius.radius12))
            .background(color = Color(0x1FFF4037))
            .clickable { context.openNotificationSettings() }
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
    notificationSettings: ImmutableList<NotificationSetting>,
    onIntent: (NotificationSettingIntent) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(
            items = notificationSettings,
            key = { it.hashCode() },
            contentType = { it },
        ) { notificationSetting ->
            when (notificationSetting) {
                is NotificationSetting.Title -> {
                    Spacer(Modifier.height(MoaTheme.spacing.spacing24))

                    Text(
                        text = notificationSetting.title,
                        style = MoaTheme.typography.b2_500,
                        color = MoaTheme.colors.textMediumEmphasis,
                    )
                }

                is NotificationSetting.Content -> {
                    Spacer(Modifier.height(MoaTheme.spacing.spacing8))

                    MoaRow(
                        leadingContent = {
                            Text(
                                text = notificationSetting.title,
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
                                checked = if (isNotificationEnabled) notificationSetting.checked else false,
                                onCheckedChange = {
                                    onIntent(
                                        NotificationSettingIntent.ToggleNotification(
                                            notificationSetting
                                        )
                                    )
                                },
                                enabled = isNotificationEnabled,
                            )
                        }
                    )
                }
            }
        }
    }
}

sealed interface NotificationSettingIntent {
    data object ClickBack : NotificationSettingIntent

    @JvmInline
    value class ToggleNotification(
        val notificationSetting: NotificationSetting.Content
    ) : NotificationSettingIntent
}

@Preview
@Composable
private fun NotificationSettingScreenPreview() {
    MoaTheme {
        NotificationSettingScreen(
            isNotificationEnabled = true,
            notificationSettings = persistentListOf(
                NotificationSetting.Title("서비스 알림"),
                NotificationSetting.Content(
                    type = "WORK",
                    title = "출퇴근 알림",
                    checked = true,
                ),
                NotificationSetting.Content(
                    type = "PAYDAY",
                    title = "월급날 알림",
                    checked = true,
                ),
                NotificationSetting.Title("광고성 정보 알림"),
                NotificationSetting.Content(
                    type = "MARKETING",
                    title = "혜택 및 이벤트 알림",
                    checked = true,
                ),
            ),
            onIntent = {},
        )
    }
}