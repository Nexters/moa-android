package com.moa.app.presentation.ui.home.beforework

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moa.app.core.model.onboarding.Time
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.component.MoaDateLocationBar
import com.moa.app.presentation.designsystem.component.MoaPrimaryButton
import com.moa.app.presentation.designsystem.component.MoaTimeBottomSheet
import com.moa.app.presentation.designsystem.component.MoaTooltipBanner
import com.moa.app.presentation.designsystem.component.MoaVacationButton
import com.moa.app.presentation.designsystem.theme.MoaTheme
import com.moa.app.presentation.navigation.HomeNavigation
import com.moa.app.presentation.ui.home.beforework.model.BeforeWorkIntent
import com.moa.app.presentation.ui.home.beforework.model.BeforeWorkUiState

@Composable
fun BeforeWorkScreen(
    args: HomeNavigation.BeforeWork,
    viewModel: BeforeWorkViewModel = hiltViewModel<BeforeWorkViewModel, BeforeWorkViewModel.Factory> { factory ->
        factory.create(args)
    },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BeforeWorkScreen(
        uiState = uiState,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun BeforeWorkScreen(
    uiState: BeforeWorkUiState,
    onIntent: (BeforeWorkIntent) -> Unit,
) {
    if (uiState.showTimeBottomSheet) {
        MoaTimeBottomSheet(
            time = Time(
                startHour = uiState.startHour,
                startMinute = uiState.startMinute,
                endHour = uiState.endHour,
                endMinute = uiState.endMinute,
            ),
            title = stringResource(R.string.before_work_time_bottom_sheet_title),
            onPositive = { time ->
                onIntent(
                    BeforeWorkIntent.UpdateWorkTime(
                        startHour = time.startHour,
                        startMinute = time.startMinute,
                        endHour = time.endHour,
                        endMinute = time.endMinute,
                    )
                )
            },
            onDismissRequest = { onIntent(BeforeWorkIntent.DismissTimeBottomSheet) },
        )
    }

    if (uiState.isWorkDay) {
        WorkDayContent(uiState = uiState, onIntent = onIntent)
    } else {
        DayOffContent(uiState = uiState, onIntent = onIntent)
    }
}

@Composable
private fun WorkDayContent(
    uiState: BeforeWorkUiState,
    onIntent: (BeforeWorkIntent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MoaTheme.spacing.spacing20),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(MoaTheme.spacing.spacing16))

        MoaDateLocationBar(
            date = uiState.dateDisplay,
            location = uiState.location,
        )

        Spacer(Modifier.height(MoaTheme.spacing.spacing32))

        AccumulatedSalarySection(
            month = uiState.month,
            accumulatedSalary = uiState.accumulatedSalary,
            todayEarnedSalary = uiState.todayEarnedSalaryDisplay,
            isWorkDay = true,
        )

        Spacer(Modifier.height(MoaTheme.spacing.spacing32))

        TodayInfoCard(
            todaySalary = uiState.todaySalary,
            workTime = uiState.workTimeDisplay,
            onWorkTimeClick = { onIntent(BeforeWorkIntent.ClickWorkTime) },
        )

        Spacer(Modifier.weight(1f))

        MoaTooltipBanner(
            text = stringResource(R.string.before_work_auto_clock_in, uiState.autoClockInTime),
        )

        Spacer(Modifier.height(MoaTheme.spacing.spacing12))

        MoaPrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            onClick = { onIntent(BeforeWorkIntent.ClickEarlyClockIn) },
        ) {
            Text(
                text = stringResource(R.string.before_work_early_clock_in),
                style = MoaTheme.typography.t3_700,
            )
        }

        Spacer(Modifier.height(MoaTheme.spacing.spacing16))

        Text(
            modifier = Modifier
                .clickable { onIntent(BeforeWorkIntent.ClickVacation) }
                .padding(vertical = MoaTheme.spacing.spacing8),
            text = stringResource(R.string.before_work_vacation_today),
            style = MoaTheme.typography.b2_500,
            color = MoaTheme.colors.textMediumEmphasis,
            textDecoration = TextDecoration.Underline,
        )

        Spacer(Modifier.height(MoaTheme.spacing.spacing24))
    }
}

@Composable
private fun DayOffContent(
    uiState: BeforeWorkUiState,
    onIntent: (BeforeWorkIntent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MoaTheme.spacing.spacing20),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(MoaTheme.spacing.spacing16))

        MoaDateLocationBar(
            date = uiState.dateDisplay,
            location = uiState.location,
        )

        Spacer(Modifier.height(MoaTheme.spacing.spacing32))

        AccumulatedSalarySection(
            month = uiState.month,
            accumulatedSalary = uiState.accumulatedSalary,
            todayEarnedSalary = uiState.todayEarnedSalaryDisplay,
            isWorkDay = false,
        )

        Spacer(Modifier.height(MoaTheme.spacing.spacing32))

        DayOffInfoCard()

        Spacer(Modifier.weight(1f))

        MoaVacationButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            onClick = { onIntent(BeforeWorkIntent.ClickClockInOnDayOff) },
        ) {
            Text(
                text = stringResource(R.string.before_work_day_off_clock_in),
                style = MoaTheme.typography.t3_700,
            )
        }

        Spacer(Modifier.height(MoaTheme.spacing.spacing24))
    }
}

@Composable
private fun DayOffInfoCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MoaTheme.colors.containerPrimary,
                shape = RoundedCornerShape(MoaTheme.radius.radius16),
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = MoaTheme.radius.radius16, bottomEnd = MoaTheme.radius.radius16))
                .padding(
                    horizontal = MoaTheme.spacing.spacing20,
                    vertical = MoaTheme.spacing.spacing16,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.before_work_work_time),
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textMediumEmphasis,
            )

            Spacer(Modifier.weight(1f))

            Text(
                text = stringResource(R.string.before_work_day_off_no_schedule),
                style = MoaTheme.typography.t3_700,
                color = MoaTheme.colors.textHighEmphasis,
            )

            Spacer(Modifier.width(8.dp))

            Icon(
                painter = painterResource(R.drawable.ic_24_chevron_right),
                contentDescription = stringResource(R.string.before_work_edit_icon_description),
                tint = MoaTheme.colors.textLowEmphasis,
            )
        }
    }
}

@Composable
private fun AccumulatedSalarySection(
    month: Int,
    accumulatedSalary: String,
    todayEarnedSalary: String?,
    isWorkDay: Boolean = true,
) {
    val coinImage = if (isWorkDay) {
        R.drawable.icon_empty_coin
    } else {
        R.drawable.ic_vacation_coin
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier.size(80.dp),
            painter = painterResource(coinImage),
            contentDescription = stringResource(R.string.before_work_coin_icon_description),
        )

        Spacer(Modifier.height(MoaTheme.spacing.spacing16))

        Text(
            text = stringResource(R.string.before_work_accumulated_salary_title, month),
            style = MoaTheme.typography.b1_500,
            color = MoaTheme.colors.textMediumEmphasis,
        )

        Spacer(Modifier.height(MoaTheme.spacing.spacing4))

        val salaryColor = if (isWorkDay) {
            MoaTheme.colors.textGreen
        } else {
            MoaTheme.colors.textBlue
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = accumulatedSalary,
                style = MoaTheme.typography.h1_700,
                color = salaryColor,
            )

            Spacer(Modifier.width(4.dp))

            Text(
                text = stringResource(R.string.before_work_currency_won),
                style = MoaTheme.typography.t2_700,
                color = MoaTheme.colors.textHighEmphasis,
            )

            if (todayEarnedSalary != null) {
                Spacer(Modifier.width(MoaTheme.spacing.spacing8))

                Box(
                    modifier = Modifier
                        .background(
                            color = MoaTheme.colors.textBlue,
                            shape = RoundedCornerShape(MoaTheme.radius.radius999),
                        )
                        .padding(
                            horizontal = MoaTheme.spacing.spacing8,
                            vertical = MoaTheme.spacing.spacing4,
                        ),
                ) {
                    Text(
                        text = todayEarnedSalary,
                        style = MoaTheme.typography.b2_500,
                        color = MoaTheme.colors.textHighEmphasis,
                    )
                }
            }
        }
    }
}

@Composable
private fun TodayInfoCard(
    todaySalary: String,
    workTime: String,
    onWorkTimeClick: () -> Unit,
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
                text = stringResource(R.string.before_work_today_salary),
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
                .clip(RoundedCornerShape(bottomStart = MoaTheme.radius.radius16, bottomEnd = MoaTheme.radius.radius16))
                .clickable { onWorkTimeClick() }
                .padding(
                    horizontal = MoaTheme.spacing.spacing20,
                    vertical = MoaTheme.spacing.spacing16,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.before_work_work_time),
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textMediumEmphasis,
            )

            Spacer(Modifier.width(MoaTheme.spacing.spacing12))

            Text(
                text = workTime,
                style = MoaTheme.typography.t3_700,
                color = MoaTheme.colors.textHighEmphasis,
            )

            Spacer(Modifier.weight(1f))

            Icon(
                painter = painterResource(R.drawable.ic_24_chevron_right),
                contentDescription = stringResource(R.string.before_work_edit_icon_description),
                tint = MoaTheme.colors.textLowEmphasis,
            )
        }
    }
}

@Preview
@Composable
private fun BeforeWorkScreenPreview() {
    MoaTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MoaTheme.colors.bgPrimary),
        ) {
            BeforeWorkScreen(
                uiState = BeforeWorkUiState(),
                onIntent = {},
            )
        }
    }
}