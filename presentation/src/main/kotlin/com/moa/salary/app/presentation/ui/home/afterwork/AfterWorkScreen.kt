package com.moa.salary.app.presentation.ui.home.afterwork

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moa.salary.app.core.model.work.Home
import com.moa.salary.app.core.model.work.WorkdayType
import com.moa.salary.app.presentation.R
import com.moa.salary.app.presentation.designsystem.component.MoaBlueButton
import com.moa.salary.app.presentation.designsystem.component.MoaDateLocationBar
import com.moa.salary.app.presentation.designsystem.component.MoaPrimaryButton
import com.moa.salary.app.presentation.designsystem.component.MoaRollingText
import com.moa.salary.app.presentation.designsystem.theme.MoaTheme
import com.moa.salary.app.presentation.model.HomeNavigation
import kotlinx.collections.immutable.persistentListOf

@Composable
fun AfterWorkScreen(
    args: HomeNavigation.AfterWork,
    viewModel: AfterWorkViewModel = hiltViewModel<AfterWorkViewModel, AfterWorkViewModel.Factory> { factory ->
        factory.create(args)
    },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onIntent(AfterWorkIntent.GetHome)
    }

    AfterWorkScreen(
        uiState = uiState,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun AfterWorkScreen(
    uiState: AfterWorkUiState,
    onIntent: (AfterWorkIntent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MoaTheme.spacing.spacing20),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(MoaTheme.spacing.spacing24))

        MoaDateLocationBar(
            date = uiState.dateDisplay,
            workplace = uiState.home.workplace,
        )

        Spacer(Modifier.height(MoaTheme.spacing.spacing28))

        Image(
            modifier = Modifier.size(80.dp),
            painter = painterResource(
                if (uiState.home.type == WorkdayType.WORK) {
                    R.drawable.ic_full_coin
                } else {
                    R.drawable.ic_vacation_coin
                }
            ),
            contentDescription = stringResource(R.string.after_work_coin_description),
        )

        Spacer(Modifier.height(MoaTheme.spacing.spacing16))

        Text(
            text = stringResource(R.string.after_work_accumulated_salary_title, uiState.month),
            style = MoaTheme.typography.t3_500,
            color = MoaTheme.colors.textHighEmphasis,
        )

        Spacer(Modifier.height(MoaTheme.spacing.spacing4))

        Row(verticalAlignment = Alignment.Bottom) {
            MoaRollingText(
                text = uiState.accumulatedSalary,
                textColor = if (uiState.home.type == WorkdayType.WORK) {
                    MoaTheme.colors.textGreen
                } else {
                    MoaTheme.colors.textBlue
                },
                animateOnAppear = true,
            )

            Spacer(Modifier.width(4.dp))

            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = stringResource(R.string.after_work_currency_won),
                style = MoaTheme.typography.h3_500,
                color = MoaTheme.colors.textMediumEmphasis,
            )
        }

        Spacer(Modifier.height(42.dp))

        InfoCard(
            todaySalary = uiState.todaySalary,
            workTimeOrVacation = uiState.workTimeDisplay,
        )

        Spacer(Modifier.weight(1f))

        if (uiState.home.type == WorkdayType.WORK) {
            MoaPrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onIntent(AfterWorkIntent.ClickHistory) },
            ) {
                Text(
                    text = "이번달 일정 확인하기",
                    style = MoaTheme.typography.t3_700,
                )
            }
        } else {
            MoaBlueButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onIntent(AfterWorkIntent.ClickHistory) },
            ) {
                Text(
                    text = "이번달 일정 확인하기",
                    style = MoaTheme.typography.t3_700,
                )
            }
        }

        Spacer(Modifier.height(MoaTheme.spacing.spacing24))
    }
}

@Composable
private fun InfoCard(
    todaySalary: String,
    workTimeOrVacation: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MoaTheme.colors.containerPrimary,
                shape = RoundedCornerShape(MoaTheme.radius.radius16),
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MoaTheme.spacing.spacing20,
                    vertical = MoaTheme.spacing.spacing16,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.after_work_today_salary_info_label),
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textMediumEmphasis,
            )

            Spacer(Modifier.width(MoaTheme.spacing.spacing12))

            Text(
                text = todaySalary,
                style = MoaTheme.typography.t3_700,
                color = MoaTheme.colors.textHighEmphasis,
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = MoaTheme.spacing.spacing20),
            thickness = 1.dp,
            color = MoaTheme.colors.dividerSecondary,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MoaTheme.spacing.spacing20,
                    vertical = MoaTheme.spacing.spacing16,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.after_work_work_time_label),
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textMediumEmphasis,
            )

            Spacer(Modifier.width(MoaTheme.spacing.spacing12))

            Text(
                text = workTimeOrVacation,
                style = MoaTheme.typography.t3_700,
                color = MoaTheme.colors.textHighEmphasis,
            )
        }
    }
}

sealed interface AfterWorkIntent {
    data object GetHome : AfterWorkIntent
    data object ClickHistory : AfterWorkIntent
    data object ClickMoreWork : AfterWorkIntent
    data object DismissMoreWorkBottomSheet : AfterWorkIntent
    data class ConfirmMoreWork(
        val endHour: Int,
        val endMinute: Int,
    ) : AfterWorkIntent
}

@Preview
@Composable
private fun AfterWorkScreenPreview() {
    MoaTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MoaTheme.colors.bgPrimary),
        ) {
            AfterWorkScreen(
                uiState = AfterWorkUiState(
                    home = Home(
                        workplace = "모아주식회사",
                        workedEarnings = 1000000,
                        standardSalary = 1000000,
                        dailyPay = 100000,
                        type = WorkdayType.WORK,
                        events = persistentListOf(),
                        startHour = 9,
                        startMinute = 0,
                        endHour = 18,
                        endMinute = 0,
                    ),
                ),
                onIntent = {},
            )
        }
    }
}