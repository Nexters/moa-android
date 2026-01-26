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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.component.MoaRow
import com.moa.app.presentation.designsystem.component.MoaTopAppBar
import com.moa.app.presentation.designsystem.theme.MoaTheme

@Composable
fun WorkInfoScreen(viewModel: WorkInfoViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WorkInfoScreen(
        uiState = uiState,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun WorkInfoScreen(
    uiState: WorkInfoUiState,
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

            WorkInfoAccountContent(oauthType = uiState.oauthType)

            Spacer(Modifier.height(MoaTheme.spacing.spacing24))

            WorkInfoSalaryContent(
                salary = uiState.salary,
                salaryDate = uiState.salaryDate,
                onIntent = onIntent,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing24))

            WorkInfoContent(
                workPlace = uiState.workPlace,
                workScheduleDays = uiState.workScheduleDays,
                workTime = uiState.workTime,
                lunchTime = uiState.lunchTime,
                onIntent = onIntent,
            )
        }
    }
}

@Composable
private fun WorkInfoAccountContent(oauthType: String) {
    Text(
        text = "가입 계정",
        style = MoaTheme.typography.b2_500,
        color = MoaTheme.colors.textMediumEmphasis,
    )

    Spacer(Modifier.height(MoaTheme.spacing.spacing8))

    MoaRow(
        leadingContent = {
            Text(
                text = "$oauthType 계정으로 가입",
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
private fun WorkInfoSalaryContent(
    salary: String,
    salaryDate: String,
    onIntent: (WorkInfoIntent) -> Unit,
) {
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
                text = salary,
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
                text = salaryDate,
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
private fun WorkInfoContent(
    workPlace: String,
    workScheduleDays: String,
    workTime: String,
    lunchTime: String,
    onIntent: (WorkInfoIntent) -> Unit,
) {
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
                text = workPlace,
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
                text = workScheduleDays,
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
                text = workTime,
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
                text = lunchTime,
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
        WorkInfoScreen(
            uiState = WorkInfoUiState(
                oauthType = "카카오",
                salary = "월급 · 300만원",
                salaryDate = "25일",
                workPlace = "카카오모빌리티",
                workScheduleDays = "월, 화, 수, 목, 금",
                workTime = "09:00~18:00",
                lunchTime = "12:00~13:00",
            ),
            onIntent = {},
        )
    }
}