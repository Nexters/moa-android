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
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.component.MoaRow
import com.moa.app.presentation.designsystem.component.MoaSwitch
import com.moa.app.presentation.designsystem.component.MoaTopAppBar
import com.moa.app.presentation.designsystem.theme.MoaTheme

@Composable
fun NotificationSettingScreen(viewModel: NotificationSettingViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val isNotificationEnabled = remember {
        NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    NotificationSettingScreen(
        isNotificationEnabled = isNotificationEnabled,
        onIntent = viewModel::onIntent
    )
}

@Composable
private fun NotificationSettingScreen(
    isNotificationEnabled: Boolean,
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

            NotificationSettingServiceContent(
                isNotificationEnabled = isNotificationEnabled,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing24))

            NotificationSettingMarketingContent(
                isNotificationEnabled = isNotificationEnabled,
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
private fun NotificationSettingServiceContent(
    isNotificationEnabled: Boolean,
) {
    Text(
        text = "서비스 알림",
        style = MoaTheme.typography.b2_500,
        color = MoaTheme.colors.textMediumEmphasis,
    )

    Spacer(Modifier.height(MoaTheme.spacing.spacing8))

    MoaRow(
        leadingContent = {
            Text(
                text = "출퇴근 알림",
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
                checked = true,
                onCheckedChange = {},
                enabled = isNotificationEnabled,
            )
        }
    )

    Spacer(Modifier.height(10.dp))

    MoaRow(
        leadingContent = {
            Text(
                text = "월급날 알림",
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
                checked = true,
                onCheckedChange = {},
                enabled = isNotificationEnabled,
            )
        }
    )
}

@Composable
private fun NotificationSettingMarketingContent(
    isNotificationEnabled: Boolean,
) {
    Text(
        text = "광고성 정보 알림",
        style = MoaTheme.typography.b2_500,
        color = MoaTheme.colors.textMediumEmphasis,
    )

    Spacer(Modifier.height(MoaTheme.spacing.spacing8))

    MoaRow(
        leadingContent = {
            Text(
                text = "혜택 및 이벤트 알림",
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
                checked = true,
                onCheckedChange = {},
                enabled = isNotificationEnabled,
            )
        }
    )
}

sealed interface NotificationSettingIntent {
    data object ClickBack : NotificationSettingIntent
}

@Preview
@Composable
private fun NotificationSettingScreenPreview() {
    MoaTheme {
        NotificationSettingScreen(
            isNotificationEnabled = true,
            onIntent = {},
        )
    }
}