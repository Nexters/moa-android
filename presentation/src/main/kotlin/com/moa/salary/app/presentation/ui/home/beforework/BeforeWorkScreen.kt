package com.moa.salary.app.presentation.ui.home.beforework

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moa.salary.app.core.model.onboarding.Time
import com.moa.salary.app.core.model.work.Home
import com.moa.salary.app.core.model.work.WorkdayType
import com.moa.salary.app.presentation.R
import com.moa.salary.app.presentation.designsystem.component.MoaDateLocationBar
import com.moa.salary.app.presentation.designsystem.component.MoaPrimaryButton
import com.moa.salary.app.presentation.designsystem.component.MoaTertiaryButton
import com.moa.salary.app.presentation.designsystem.component.MoaTimeBottomSheet
import com.moa.salary.app.presentation.designsystem.component.MoaTooltipBanner
import com.moa.salary.app.presentation.designsystem.theme.MoaTheme
import com.moa.salary.app.presentation.model.HomeNavigation

@Composable
fun BeforeWorkScreen(
    args: HomeNavigation.BeforeWork,
    viewModel: BeforeWorkViewModel = hiltViewModel<BeforeWorkViewModel, BeforeWorkViewModel.Factory> { factory ->
        factory.create(args)
    },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onIntent(BeforeWorkIntent.GetHome)
    }

    BeforeWorkScreen(
        uiState = uiState,
        onIntent = viewModel::onIntent,
    )

    if (uiState.showTimeBottomSheet) {
        MoaTimeBottomSheet(
            time = Time(
                startHour = uiState.home.startHour,
                startMinute = uiState.home.startMinute,
                endHour = uiState.home.endHour,
                endMinute = uiState.home.endMinute,
            ),
            title = stringResource(R.string.before_work_time_bottom_sheet_title),
            onPositive = { time ->
                viewModel.onIntent(
                    BeforeWorkIntent.UpdateWorkTime(
                        startHour = time.startHour,
                        startMinute = time.startMinute,
                        endHour = time.endHour,
                        endMinute = time.endMinute,
                    )
                )
            },
            onDismissRequest = { viewModel.onIntent(BeforeWorkIntent.DismissTimeBottomSheet) },
        )
    }
}

@Composable
private fun BeforeWorkScreen(
    uiState: BeforeWorkUiState,
    onIntent: (BeforeWorkIntent) -> Unit,
) {
    if (uiState.home.type == WorkdayType.WORK) {
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
            workplace = uiState.home.workplace,
        )

        Spacer(Modifier.height(MoaTheme.spacing.spacing32))

        AccumulatedSalarySection(
            month = uiState.month,
            accumulatedSalary = uiState.accumulatedSalary,
            additionalSalaryDisplay = uiState.additionalSalaryDisplay,
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
            modifier = Modifier.fillMaxWidth(),
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
            workplace = uiState.home.workplace,
        )

        Spacer(Modifier.height(MoaTheme.spacing.spacing32))

        AccumulatedSalarySection(
            month = uiState.month,
            accumulatedSalary = uiState.accumulatedSalary,
            additionalSalaryDisplay = uiState.additionalSalaryDisplay,
            isWorkDay = false,
        )

        Spacer(Modifier.height(MoaTheme.spacing.spacing36))

        DayOffInfoCard()

        Spacer(Modifier.weight(1f))

        MoaTooltipBanner(text = "설마 쉬는 날 일하시나요?")

        Spacer(Modifier.height(MoaTheme.spacing.spacing12))

        MoaTertiaryButton(
            modifier = Modifier.fillMaxWidth(),
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
                .clip(
                    RoundedCornerShape(
                        bottomStart = MoaTheme.radius.radius16,
                        bottomEnd = MoaTheme.radius.radius16
                    )
                )
                .padding(vertical = MoaTheme.spacing.spacing20)
                .padding(start = 16.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.before_work_work_time),
                style = MoaTheme.typography.b1_400,
                color = MoaTheme.colors.textMediumEmphasis,
            )

            Spacer(Modifier.width(MoaTheme.spacing.spacing12))

            Text(
                text = stringResource(R.string.before_work_day_off_no_schedule),
                style = MoaTheme.typography.b1_600,
                color = MoaTheme.colors.textHighEmphasis,
            )
        }
    }
}

@Composable
private fun AccumulatedSalarySection(
    month: Int,
    accumulatedSalary: String,
    additionalSalaryDisplay: String?,
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
            style = MoaTheme.typography.t3_500,
            color = MoaTheme.colors.textHighEmphasis,
        )

        Spacer(Modifier.height(MoaTheme.spacing.spacing4))

        val salaryColor = if (isWorkDay) {
            MoaTheme.colors.textGreen
        } else {
            MoaTheme.colors.textBlue
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = accumulatedSalary,
                style = MoaTheme.typography.h1_700,
                color = salaryColor,
            )

            Spacer(Modifier.width(4.dp))

            Text(
                text = stringResource(R.string.before_work_currency_won),
                style = MoaTheme.typography.h3_500,
                color = MoaTheme.colors.textMediumEmphasis,
            )
        }

        if (additionalSalaryDisplay != null) {
            Spacer(Modifier.height(MoaTheme.spacing.spacing8))

            Text(
                text = buildAnnotatedString {
                    append("기본 월급보다 ")
                    withStyle(
                        SpanStyle(
                            color = MoaTheme.colors.textHighEmphasis,
                            fontSize = MoaTheme.typography.b1_400.fontSize,
                            fontWeight = MoaTheme.typography.b1_400.fontWeight,
                        )
                    ) {
                        append(additionalSalaryDisplay)
                    }
                    append(" 더 일했어요")
                },
                style = MoaTheme.typography.b2_400,
                color = MoaTheme.colors.textMediumEmphasis,
            )
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
        Spacer(Modifier.height(MoaTheme.spacing.spacing16))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MoaTheme.spacing.spacing16),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.before_work_today_salary),
                style = MoaTheme.typography.b1_400,
                color = MoaTheme.colors.textMediumEmphasis,
            )

            Spacer(Modifier.width(MoaTheme.spacing.spacing12))

            Text(
                text = todaySalary,
                style = MoaTheme.typography.b1_600,
                color = MoaTheme.colors.textHighEmphasis,
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(
                    vertical = 14.dp,
                    horizontal = MoaTheme.spacing.spacing16
                ),
            color = MoaTheme.colors.dividerSecondary,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onWorkTimeClick() }
                .padding(
                    start = MoaTheme.spacing.spacing16,
                    end = MoaTheme.spacing.spacing12,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.before_work_work_time),
                style = MoaTheme.typography.b1_400,
                color = MoaTheme.colors.textMediumEmphasis,
            )

            Spacer(Modifier.width(MoaTheme.spacing.spacing12))

            Text(
                text = workTime,
                style = MoaTheme.typography.b1_600,
                color = MoaTheme.colors.textHighEmphasis,
            )

            Spacer(Modifier.weight(1f))

            Icon(
                painter = painterResource(R.drawable.ic_24_chevron_right),
                contentDescription = stringResource(R.string.before_work_edit_icon_description),
                tint = MoaTheme.colors.textLowEmphasis,
            )
        }

        Spacer(Modifier.height(MoaTheme.spacing.spacing16))
    }
}

sealed interface BeforeWorkIntent {
    data object GetHome : BeforeWorkIntent
    data object ClickWorkTime : BeforeWorkIntent
    data object ClickEarlyClockIn : BeforeWorkIntent
    data object ClickVacation : BeforeWorkIntent
    data object ClickClockInOnDayOff : BeforeWorkIntent
    data object DismissTimeBottomSheet : BeforeWorkIntent
    data class UpdateWorkTime(
        val startHour: Int,
        val startMinute: Int,
        val endHour: Int,
        val endMinute: Int,
    ) : BeforeWorkIntent
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
                uiState = BeforeWorkUiState(
                    home = Home(
                        workplace = "모아주식회사",
                        workedEarnings = 1000000,
                        standardSalary = 1000000,
                        dailyPay = 100000,
                        type = WorkdayType.WORK,
                        startHour = 9,
                        startMinute = 0,
                        endHour = 18,
                        endMinute = 0,
                    )
                ),
                onIntent = {},
            )
        }
    }
}