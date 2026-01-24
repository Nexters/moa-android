package com.moa.app.presentation.ui.setting.notification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.component.MoaRow
import com.moa.app.presentation.designsystem.component.MoaTopAppBar
import com.moa.app.presentation.designsystem.theme.MoaTheme

@Composable
fun NotificationSettingScreen(viewModel: NotificationSettingViewModel = hiltViewModel()) {
    NotificationSettingScreen(
        onIntent = viewModel::onIntent
    )
}

@Composable
private fun NotificationSettingScreen(
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
                            painter = painterResource(R.drawable.icon_back),
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

            NotificationSettingServiceContent()

            Spacer(Modifier.height(MoaTheme.spacing.spacing24))

            NotificationSettingMarketingContent()
        }
    }
}

@Composable
private fun NotificationSettingServiceContent() {
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
                color = MoaTheme.colors.textHighEmphasis,
            )
        },
        trailingContent = {

        }
    )

    Spacer(Modifier.height(10.dp))

    MoaRow(
        leadingContent = {
            Text(
                text = "월급날 알림",
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textHighEmphasis,
            )
        },
        trailingContent = {

        }
    )
}

@Composable
private fun NotificationSettingMarketingContent() {
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
                color = MoaTheme.colors.textHighEmphasis,
            )
        },
        trailingContent = {
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
            onIntent = {},
        )
    }
}