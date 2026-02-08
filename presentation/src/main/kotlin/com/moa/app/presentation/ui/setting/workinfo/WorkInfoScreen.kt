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
import com.moa.app.core.extensions.makePriceString
import com.moa.app.core.model.onboarding.Payroll
import com.moa.app.core.model.onboarding.Time
import com.moa.app.core.model.onboarding.WorkPolicy
import com.moa.app.core.model.setting.OAuthType
import com.moa.app.core.model.setting.WorkInfo
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.component.MoaRow
import com.moa.app.presentation.designsystem.component.MoaTopAppBar
import com.moa.app.presentation.designsystem.theme.MoaTheme
import kotlinx.collections.immutable.persistentListOf

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

            WorkInfoAccountContent(oAuthType = uiState.workInfo?.oAuthType)

            Spacer(Modifier.height(MoaTheme.spacing.spacing24))

            WorkInfoSalaryContent(
                salary = "${uiState.workInfo?.payroll?.salaryType?.title} · ${uiState.workInfo?.payroll?.salary?.makePriceString()}",
                salaryDate = uiState.workInfo?.payroll?.paydayDay ?: 0,
                onIntent = onIntent,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing24))

            WorkInfoContent(
                workPlace = uiState.workInfo?.workPlace ?: "",
                workScheduleDays = uiState.workInfo
                    ?.workPolicy
                    ?.workScheduleDays
                    ?.joinToString { it.day }
                    ?: "",
                workTime = uiState.workInfo?.workPolicy?.time?.getFormattedTimeRange() ?: "",
                onIntent = onIntent,
            )
        }
    }
}

@Composable
private fun WorkInfoAccountContent(oAuthType: OAuthType?) {
    Text(
        text = "가입 계정",
        style = MoaTheme.typography.b2_500,
        color = MoaTheme.colors.textMediumEmphasis,
    )

    if (oAuthType != null) {
        Spacer(Modifier.height(MoaTheme.spacing.spacing8))

        MoaRow(
            leadingContent = {
                Text(
                    text = "${oAuthType.title} 계정으로 가입",
                    style = MoaTheme.typography.b1_500,
                    color = MoaTheme.colors.textHighEmphasis,
                )
            },
            trailingContent = {
                Image(
                    painter = painterResource(
                        when (oAuthType) {
                            OAuthType.KAKAO -> R.drawable.ic_24_kakao
                        }
                    ),
                    contentDescription = "Chevron Right",
                )
            }
        )
    }
}

@Composable
private fun WorkInfoSalaryContent(
    salary: String,
    salaryDate: Int,
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
                painter = painterResource(R.drawable.ic_24_chevron_right),
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
                text = "${salaryDate}일",
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textGreen,
            )
        },
        trailingContent = {
            Image(
                painter = painterResource(R.drawable.ic_24_chevron_right),
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
                painter = painterResource(R.drawable.ic_24_chevron_right),
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
                painter = painterResource(R.drawable.ic_24_chevron_right),
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
                painter = painterResource(R.drawable.ic_24_chevron_right),
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
                workInfo = WorkInfo(
                    oAuthType = OAuthType.KAKAO,
                    payroll = Payroll(
                        salary = "40000000",
                        salaryType = Payroll.SalaryType.ANNUAL,
                    ),
                    workPlace = "집계리아",
                    workPolicy = WorkPolicy(
                        workScheduleDays = persistentListOf(
                            WorkPolicy.WorkScheduleDay.MON,
                            WorkPolicy.WorkScheduleDay.TUE,
                            WorkPolicy.WorkScheduleDay.WED,
                            WorkPolicy.WorkScheduleDay.THU,
                            WorkPolicy.WorkScheduleDay.FRI,
                        ),
                        time = Time(
                            startHour = 9,
                            startMinute = 0,
                            endHour = 18,
                            endMinute = 0,
                        )
                    )
                )
            ),
            onIntent = {},
        )
    }
}