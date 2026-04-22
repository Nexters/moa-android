package com.moa.salary.app.presentation.ui.history.calendar

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kizitonwose.calendar.core.yearMonth
import com.moa.salary.app.core.extensions.formatCurrency
import com.moa.salary.app.core.model.work.Calendar
import com.moa.salary.app.core.model.work.Event
import com.moa.salary.app.core.model.work.MonthlyInfo
import com.moa.salary.app.core.model.work.Workday
import com.moa.salary.app.core.model.work.WorkdayStatus
import com.moa.salary.app.core.model.work.WorkdayType
import com.moa.salary.app.presentation.R
import com.moa.salary.app.presentation.designsystem.component.MoaCalendar
import com.moa.salary.app.presentation.designsystem.component.MoaMonthNavigator
import com.moa.salary.app.presentation.designsystem.component.MoaTopAppBar
import com.moa.salary.app.presentation.designsystem.theme.Gray40
import com.moa.salary.app.presentation.designsystem.theme.MoaTheme
import com.moa.salary.app.presentation.model.PosthogEvent
import kotlinx.collections.immutable.persistentMapOf
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarScreen(viewModel: CalendarViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onIntent(CalendarIntent.GetCalendar)
    }

    CalendarScreen(
        uiState = uiState,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun CalendarScreen(
    uiState: CalendarUiState,
    onIntent: (CalendarIntent) -> Unit,
) {
    Scaffold(
        topBar = {
            MoaTopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = { onIntent(CalendarIntent.ClickBack) }
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
                        month = uiState.selectedDate.yearMonth.monthValue,
                        previousEnabled = uiState.calendar?.joinedAt?.yearMonth?.let { joinedAtMonth ->
                            uiState.selectedDate.yearMonth.isAfter(joinedAtMonth)
                        } ?: false,
                        nextEnabled = uiState.selectedDate.yearMonth.isBefore(
                            YearMonth.now().plusMonths(12)
                        ),
                        onPreviousClick = {
                            onIntent(
                                CalendarIntent.SetDate(
                                    uiState.selectedDate.minusMonths(1)
                                )
                            )
                        },
                        onNextClick = {
                            onIntent(
                                CalendarIntent.SetDate(
                                    uiState.selectedDate.plusMonths(1)
                                )
                            )
                        },
                    )

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                color = MoaTheme.colors.containerSecondary,
                                shape = CircleShape,
                            )
                            .clickable {
                                val workday = uiState.calendar?.workdays[uiState.selectedDate]
                                if (workday != null) {
                                    onIntent(CalendarIntent.SendEvent(CalendarEvent.ClickEdit))
                                    onIntent(CalendarIntent.ClickWorkday(workday))
                                }
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_20_edit),
                            contentDescription = null,
                        )
                    }
                }

                if (uiState.calendar != null) {
                    Spacer(Modifier.height(MoaTheme.spacing.spacing20))

                    CalendarMonthlyInfoCard(monthlyInfo = uiState.calendar.monthlyInfo)

                    Spacer(Modifier.height(MoaTheme.spacing.spacing32))
                    MoaCalendar(
                        joinedAt = uiState.calendar.joinedAt,
                        selectedDate = uiState.selectedDate,
                        selectedYearMonth = uiState.selectedDate.yearMonth,
                        workdays = uiState.calendar.workdays,
                        onScrollYearMonth = { newYearMonth ->
                            val currentYearMonth = uiState.selectedDate.yearMonth

                            if (newYearMonth != currentYearMonth) {
                                val newDate = if (newYearMonth.isAfter(currentYearMonth)) {
                                    uiState.selectedDate.plusMonths(1)
                                } else {
                                    uiState.selectedDate.minusMonths(1)
                                }
                                onIntent(CalendarIntent.SetDate(newDate))
                            }
                        },
                        onClickDate = { onIntent(CalendarIntent.SetDate(it)) }
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
                workday = uiState.calendar?.workdays[uiState.selectedDate],
                onIntent = onIntent,
            )
        }
    }
}

@Composable
private fun CalendarMonthlyInfoCard(monthlyInfo: MonthlyInfo) {
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
    workday: Workday?,
    onIntent: (CalendarIntent) -> Unit,
) {
    if (workday != null) {
        val info = when (workday.type) {
            WorkdayType.WORK -> {
                when (workday.status) {
                    WorkdayStatus.SCHEDULED -> {
                        Triple(
                            R.drawable.ic_40_working_yet,
                            stringResource(R.string.history_schedule_work_scheduled),
                            "${workday.clockInTime}~${workday.clockOutTime}",
                        )
                    }

                    WorkdayStatus.COMPLETED -> {
                        Triple(
                            R.drawable.ic_40_working_done,
                            stringResource(R.string.history_schedule_work_completed),
                            "${workday.clockInTime}~${workday.clockOutTime}",
                        )
                    }

                    WorkdayStatus.NONE -> null
                }
            }

            WorkdayType.VACATION -> {
                Triple(
                    R.drawable.ic_40_vacation,
                    stringResource(R.string.history_schedule_vacation),
                    "${workday.clockInTime}~${workday.clockOutTime}",
                )
            }

            WorkdayType.NONE -> null
        }

        if (info != null) {
            WorkdayItem(
                imgRes = info.first,
                title = info.second,
                content = info.third,
                onClick = {
                    onIntent(CalendarIntent.SendEvent(CalendarEvent.ClickList))
                    onIntent(CalendarIntent.ClickWorkday(workday))
                },
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing12))
        } else {
            WorkdayEmpty()
        }

        if (workday.events.contains(Event.PAYDAY)) {
            WorkdayItem(
                imgRes = R.drawable.ic_40_salary_day,
                title = stringResource(R.string.history_schedule_payday, currentDay),
                content = "+ ${formatCurrency(totalPay * 10000L)}원",
                onClick = { onIntent(CalendarIntent.ClickPayday(currentDay)) },
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing12))
        }
    } else {
        WorkdayEmpty()
    }
}

@Composable
private fun WorkdayEmpty() {
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
private fun WorkdayItem(
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

sealed interface CalendarIntent {
    @JvmInline
    value class SendEvent(val event: PosthogEvent) : CalendarIntent

    data object GetCalendar : CalendarIntent
    data object ClickBack : CalendarIntent

    @JvmInline
    value class SetDate(val date: LocalDate) : CalendarIntent

    @JvmInline
    value class ClickWorkday(val workday: Workday) : CalendarIntent

    @JvmInline
    value class ClickPayday(val day: Int) : CalendarIntent
}

sealed class CalendarEvent(
    override val event: String
) : PosthogEvent {
    data object ClickEdit : CalendarEvent(event = "calendar_edit_clicked")
    data object ClickList : CalendarEvent(event = "calendar_list_clicked")
}

@Preview
@Composable
private fun CalendarScreenPreview() {
    MoaTheme {
        CalendarScreen(
            uiState = CalendarUiState(
                calendar = Calendar(
                    monthlyInfo = MonthlyInfo(
                        accumulatedPay = "200",
                        accumulatedWorkTime = "200",
                        totalPay = "300",
                        totalWorkTime = "300",
                    ),
                    workdays = persistentMapOf(),
                    joinedAt = LocalDate.now().minusDays(100),
                ),
                selectedDate = LocalDate.now(),
            ),
            onIntent = {},
        )
    }
}