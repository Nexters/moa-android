package com.moa.app.presentation.ui.onboarding.notification

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.component.MoaPrimaryButton
import com.moa.app.presentation.designsystem.component.MoaTopAppBar
import com.moa.app.presentation.designsystem.theme.MoaTheme

@Composable
fun NotificationScreen(viewModel: NotificationViewModel = hiltViewModel()) {
    NotificationScreen(
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun NotificationScreen(
    onIntent: (NotificationIntent) -> Unit,
) {
    Scaffold(
        topBar = {
            MoaTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { }) {
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

            Text(
                text = "근무 시간에 맞춰\n알림을 보내드릴게요!",
                color = MoaTheme.colors.textHighEmphasis,
                style = MoaTheme.typography.t1_700,
            )

            Spacer(Modifier.weight(1f))

            MoaPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                onClick = {
                    // TODO 알림 권한 요청
                },
            ) {
                Text(
                    text = "알림 받을게요",
                    style = MoaTheme.typography.t3_700,
                )
            }

            Spacer(Modifier.height(MoaTheme.spacing.spacing8))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onIntent(NotificationIntent.ClickNext) }
                    .padding(bottom = MoaTheme.spacing.spacing24),
                text = "다음에 할게요",
                style = MoaTheme.typography.t3_700.copy(textDecoration = TextDecoration.Underline),
                color = MoaTheme.colors.textMediumEmphasis,
                textAlign = TextAlign.Center,
            )
        }
    }
}

sealed interface NotificationIntent {
    data object ClickBack : NotificationIntent
    data object ClickNext : NotificationIntent
}

@Preview
@Composable
private fun NotificationScreenPreview() {
    MoaTheme {
        NotificationScreen(onIntent = {})
    }
}