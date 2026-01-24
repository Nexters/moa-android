package com.moa.app.presentation.ui.setting.workinfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
fun WorkInfoScreen(viewModel: WorkInfoViewModel = hiltViewModel()) {

    WorkInfoScreen(
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun WorkInfoScreen(
    onIntent: (WorkInfoIntent) -> Unit,
) {
    Scaffold(
        topBar = {
            MoaTopAppBar(
                title = {
                    Text(
                        text = "월급 · 근무 정보",
                        style = MoaTheme.typography.t3_500,
                        color = MoaTheme.colors.textHighEmphasis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onIntent(WorkInfoIntent.ClickBack) }) {
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

            WorkInfoAccountContent()

            Spacer(Modifier.height(MoaTheme.spacing.spacing24))

            WorkInfoSalaryContent(onIntent = onIntent)

            Spacer(Modifier.height(MoaTheme.spacing.spacing24))

            WorkInfoContent(onIntent = onIntent)
        }
    }
}

@Composable
private fun WorkInfoAccountContent() {
    Text(
        text = "앱 설정",
        style = MoaTheme.typography.b2_500,
        color = MoaTheme.colors.textMediumEmphasis,
    )

    Spacer(Modifier.height(MoaTheme.spacing.spacing8))

    MoaRow(
        leadingContent = {
            Text(
                text = "카카오 계정으로 가입",
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textHighEmphasis,
            )
        },
        trailingContent = {
            Image(
                painter = painterResource(R.drawable.icon_chevron_right),
                contentDescription = "Chevron Right",
            )
        }
    )
}

@Composable
private fun WorkInfoSalaryContent(onIntent: (WorkInfoIntent) -> Unit) {
    Text(
        text = "월급 정보",
        style = MoaTheme.typography.b2_500,
        color = MoaTheme.colors.textMediumEmphasis,
    )

    Spacer(Modifier.height(MoaTheme.spacing.spacing8))

    MoaRow(
        modifier = Modifier.clickable { onIntent(WorkInfoIntent.ClickSalary) },
        leadingContent = {
            Text(
                text = "급여",
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textHighEmphasis,
            )
        },
        subTrailingContent = {
            Text(
                text = "월급 · 400만원",
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textGreen,
            )
        },
        trailingContent = {
            Image(
                painter = painterResource(R.drawable.icon_chevron_right),
                contentDescription = "Chevron Right",
            )
        }
    )

    Spacer(Modifier.height(10.dp))

    MoaRow(
        modifier = Modifier.clickable { onIntent(WorkInfoIntent.ClickSalaryDate) },
        leadingContent = {
            Text(
                text = "월급일",
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textHighEmphasis,
            )
        },
        subTrailingContent = {
            Text(
                text = "25일",
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textGreen,
            )
        },
        trailingContent = {
            Image(
                painter = painterResource(R.drawable.icon_chevron_right),
                contentDescription = "Chevron Right",
            )
        }
    )
}

@Composable
private fun WorkInfoContent(onIntent: (WorkInfoIntent) -> Unit) {
    Text(
        text = "근무 정보",
        style = MoaTheme.typography.b2_500,
        color = MoaTheme.colors.textMediumEmphasis,
    )

    Spacer(Modifier.height(MoaTheme.spacing.spacing8))

    MoaRow(
        modifier = Modifier.clickable { onIntent(WorkInfoIntent.ClickWorkplace) },
        leadingContent = {
            Text(
                text = "근무지",
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textHighEmphasis,
            )
        },
        subTrailingContent = {
            Text(
                text = "집계리아",
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textGreen,
            )
        },
        trailingContent = {
            Image(
                painter = painterResource(R.drawable.icon_chevron_right),
                contentDescription = "Chevron Right",
            )
        }
    )

    Spacer(Modifier.height(10.dp))

    MoaRow(
        modifier = Modifier.clickable { onIntent(WorkInfoIntent.ClickWorkSchedule) },
        leadingContent = {
            Text(
                text = "근무 요일",
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textHighEmphasis,
            )
        },
        subTrailingContent = {
            Text(
                text = "월, 화, 수, 목, 금",
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textGreen,
            )
        },
        trailingContent = {
            Image(
                painter = painterResource(R.drawable.icon_chevron_right),
                contentDescription = "Chevron Right",
            )
        }
    )

    Spacer(Modifier.height(10.dp))

    MoaRow(
        modifier = Modifier.clickable { onIntent(WorkInfoIntent.ClickWorkSchedule) },
        leadingContent = {
            Text(
                text = "근무 시간",
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textHighEmphasis,
            )
        },
        subTrailingContent = {
            Text(
                text = "09:00 ~ 18:00",
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textGreen,
            )
        },
        trailingContent = {
            Image(
                painter = painterResource(R.drawable.icon_chevron_right),
                contentDescription = "Chevron Right",
            )
        }
    )

    Spacer(Modifier.height(10.dp))

    MoaRow(
        modifier = Modifier.clickable { onIntent(WorkInfoIntent.ClickWorkSchedule) },
        leadingContent = {
            Text(
                text = "점심 시간",
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textHighEmphasis,
            )
        },
        subTrailingContent = {
            Text(
                text = "12:00 ~ 13:00",
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textGreen,
            )
        },
        trailingContent = {
            Image(
                painter = painterResource(R.drawable.icon_chevron_right),
                contentDescription = "Chevron Right",
            )
        }
    )
}

sealed interface WorkInfoIntent {
    data object ClickBack : WorkInfoIntent
    data object ClickSalary : WorkInfoIntent
    data object ClickSalaryDate : WorkInfoIntent
    data object ClickWorkplace : WorkInfoIntent
    data object ClickWorkSchedule : WorkInfoIntent
}

@Preview
@Composable
private fun WorkInfoScreenPreview() {
    MoaTheme {
        WorkInfoScreen(onIntent = {})
    }
}