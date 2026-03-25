package com.moa.salary.app.presentation.ui.history

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kizitonwose.calendar.core.yearMonth
import com.moa.salary.app.core.extensions.formatCurrency
import com.moa.salary.app.core.model.calendar.Calendar
import com.moa.salary.app.core.model.calendar.CalendarStatus
import com.moa.salary.app.core.model.calendar.Event
import com.moa.salary.app.core.model.calendar.MonthlyInfo
import com.moa.salary.app.core.model.calendar.Schedule
import com.moa.salary.app.core.model.work.WorkdayType
import com.moa.salary.app.presentation.R
import com.moa.salary.app.presentation.designsystem.component.MoaCalendar
import com.moa.salary.app.presentation.designsystem.component.MoaMonthNavigator
import com.moa.salary.app.presentation.designsystem.component.MoaTopAppBar
import com.moa.salary.app.presentation.designsystem.theme.Gray40
import com.moa.salary.app.presentation.designsystem.theme.MoaTheme
import kotlinx.collections.immutable.persistentMapOf
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun History2Screen(viewModel: History2ViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onIntent(History2Intent.GetCalendar)
    }

    History2Screen(
        uiState = uiState,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun History2Screen(
    uiState: History2UiState,
    onIntent: (History2Intent) -> Unit,
) {
    Scaffold(
        topBar = {
            MoaTopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = { onIntent(History2Intent.ClickBack) }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_24_arrow_left),
                            contentDescription = stringResource(R.string.history_back_description),
                            tint = MoaTheme.colors.textHighEmphasis,
                        )
                    }
                },
                containerColor = MoaTheme.colors.containerPrimary,
            )
        },
        containerColor = MoaTheme.colors.bgPrimary,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
                    .background(MoaTheme.colors.containerPrimary)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MoaTheme.spacing.spacing16),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    MoaMonthNavigator(
                        month = uiState.selectedYearMonth.monthValue,
                        previousEnabled = uiState.calendar?.joinedAt?.yearMonth?.let { joinedAtMonth ->
                            uiState.selectedYearMonth.isAfter(joinedAtMonth)
                        } ?: false,
                        nextEnabled = uiState.selectedYearMonth.isBefore(
                            YearMonth.now().plusMonths(12)
                        ),
                        onPreviousClick = {
                            onIntent(
                                History2Intent.SetYearMonth(
                                    uiState.selectedYearMonth.minusMonths(
                                        1
                                    )
                                )
                            )
                        },
                        onNextClick = {
                            onIntent(
                                History2Intent.SetYearMonth(
                                    uiState.selectedYearMonth.plusMonths(
                                        1
                                    )
                                )
                            )
                        },
                    )

                    IconButton(
                        onClick = {
                            val schedule = uiState.calendar?.schedules[uiState.selectedDate]
                            if (schedule != null) {
                                onIntent(History2Intent.ClickSchedule(schedule))
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_plus),
                            contentDescription = stringResource(R.string.history_add_schedule_description),
                            tint = Color.Unspecified,
                        )
                    }
                }

                if (uiState.calendar != null) {
                    Spacer(Modifier.height(MoaTheme.spacing.spacing20))

                    HistoryMonthlyInfoCard(monthlyInfo = uiState.calendar.monthlyInfo)

                    Spacer(Modifier.height(MoaTheme.spacing.spacing32))

                    MoaCalendar(
                        joinedAt = uiState.calendar.joinedAt,
                        selectedDate = uiState.selectedDate,
                        selectedYearMonth = uiState.selectedYearMonth,
                        schedules = uiState.calendar.schedules,
                        onScrollYearMonth = { onIntent(History2Intent.SetYearMonth(it)) },
                        onClickDate = { onIntent(History2Intent.ClickDate(it)) }
                    )
                }
            }

            Spacer(Modifier.height(MoaTheme.spacing.spacing28))

            Text(
                modifier = Modifier.padding(start = MoaTheme.spacing.spacing16),
                text = uiState.selectedDate.toString(),
                style = MoaTheme.typography.b1_500,
                color = Gray40,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing20))

            ScheduleItems(
                currentDay = uiState.selectedDate.dayOfMonth,
                totalPay = uiState.calendar?.monthlyInfo?.totalPay?.toInt() ?: 0,
                schedule = uiState.calendar?.schedules[uiState.selectedDate],
                onIntent = onIntent,
            )
        }
    }
}

@Composable
private fun HistoryMonthlyInfoCard(monthlyInfo: MonthlyInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MoaTheme.spacing.spacing16)
            .clip(RoundedCornerShape(MoaTheme.radius.radius16))
            .background(MoaTheme.colors.containerSecondary)
            .padding(
                horizontal = MoaTheme.spacing.spacing20,
                vertical = MoaTheme.spacing.spacing12,
            ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(R.string.history_work_hours_label),
                style = MoaTheme.typography.b2_400,
                color = MoaTheme.colors.textMediumEmphasis,
            )
            Row {
                Text(
                    text = monthlyInfo.accumulatedWorkTime,
                    style = MoaTheme.typography.b1_600,
                    color = MoaTheme.colors.textGreen,
                )
                Text(
                    text = " / ${monthlyInfo.totalWorkTime}시간",
                    style = MoaTheme.typography.b1_400,
                    color = MoaTheme.colors.textMediumEmphasis,
                )
            }
        }

        Spacer(Modifier.height(MoaTheme.spacing.spacing8))

        HorizontalDivider(color = MoaTheme.colors.dividerPrimary)

        Spacer(Modifier.height(MoaTheme.spacing.spacing8))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(R.string.history_salary_label),
                style = MoaTheme.typography.b2_400,
                color = MoaTheme.colors.textMediumEmphasis,
            )
            Row {
                Text(
                    text = monthlyInfo.accumulatedPay,
                    style = MoaTheme.typography.b1_600,
                    color = MoaTheme.colors.textGreen,
                )
                Text(
                    text = " / ${monthlyInfo.totalPay}만원",
                    style = MoaTheme.typography.b1_400,
                    color = MoaTheme.colors.textMediumEmphasis,
                )
            }
        }
    }
}

@Composable
private fun ScheduleItems(
    currentDay: Int,
    totalPay: Int,
    schedule: Schedule?,
    onIntent: (History2Intent) -> Unit,
) {
    if (schedule != null) {
        val info = when (schedule.type) {
            WorkdayType.WORK -> {
                when (schedule.status) {
                    CalendarStatus.SCHEDULED -> {
                        Triple(
                            R.drawable.ic_40_working_yet,
                            stringResource(R.string.history_schedule_work_scheduled),
                            schedule.workTime,
                        )
                    }

                    CalendarStatus.COMPLETED -> {
                        Triple(
                            R.drawable.ic_40_working_done,
                            stringResource(R.string.history_schedule_work_completed),
                            schedule.workTime,
                        )
                    }

                    CalendarStatus.NONE -> null
                }
            }

            WorkdayType.VACATION -> {
                Triple(
                    R.drawable.ic_40_vacation,
                    stringResource(R.string.history_schedule_vacation),
                    schedule.workTime
                )
            }

            WorkdayType.NONE -> null
        }

        if (info != null) {
            ScheduleItem(
                imgRes = info.first,
                title = info.second,
                content = info.third,
                onClick = { onIntent(History2Intent.ClickSchedule(schedule)) },
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing12))
        } else {
            ScheduleEmpty()
        }

        if (schedule.events.contains(Event.PAYDAY)) {
            ScheduleItem(
                imgRes = R.drawable.ic_40_salary_day,
                title = stringResource(R.string.history_schedule_payday, currentDay),
                content = "+ ${formatCurrency(totalPay * 10000L)}원",
                onClick = { onIntent(History2Intent.ClickPayday(currentDay)) },
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing12))
        }
    } else {
        ScheduleEmpty()
    }
}

@Composable
private fun ScheduleEmpty() {
    Spacer(Modifier.height(MoaTheme.spacing.spacing20))
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(R.string.history_no_schedule),
        style = MoaTheme.typography.b1_500,
        color = MoaTheme.colors.textLowEmphasis,
        textAlign = TextAlign.Center,
    )
    Spacer(Modifier.height(MoaTheme.spacing.spacing20))
}

@Composable
private fun ScheduleItem(
    @DrawableRes imgRes: Int,
    title: String,
    content: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MoaTheme.spacing.spacing16)
            .clip(RoundedCornerShape(MoaTheme.radius.radius16))
            .background(MoaTheme.colors.containerPrimary)
            .clickable(onClick = onClick)
            .padding(MoaTheme.spacing.spacing16),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Image(
            painter = painterResource(imgRes),
            contentDescription = null,
        )

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = title,
                style = MoaTheme.typography.c1_400,
                color = MoaTheme.colors.textLowEmphasis,
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = content,
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textHighEmphasis,
            )
        }

        Image(
            painter = painterResource(R.drawable.ic_24_chevron_right),
            contentDescription = stringResource(R.string.history_schedule_detail_description),
        )
    }
}

sealed interface History2Intent {
    data object GetCalendar : History2Intent
    data object ClickBack : History2Intent

    @JvmInline
    value class SetYearMonth(val yearMonth: YearMonth) : History2Intent

    @JvmInline
    value class ClickDate(val date: LocalDate) : History2Intent

    @JvmInline
    value class ClickSchedule(val schedule: Schedule) : History2Intent

    @JvmInline
    value class ClickPayday(val day: Int) : History2Intent
}

@Preview
@Composable
private fun History2ScreenPreview() {
    MoaTheme {
        History2Screen(
            uiState = History2UiState(
                calendar = Calendar(
                    monthlyInfo = MonthlyInfo(
                        accumulatedPay = "200",
                        accumulatedWorkTime = "200",
                        totalPay = "300",
                        totalWorkTime = "300",
                    ),
                    schedules = persistentMapOf(),
                    joinedAt = LocalDate.now().minusDays(100),
                ),
                selectedDate = LocalDate.now(),
                selectedYearMonth = YearMonth.now(),
            ),
            onIntent = {},
        )
    }
}