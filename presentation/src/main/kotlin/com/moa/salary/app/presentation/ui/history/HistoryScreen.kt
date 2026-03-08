package com.moa.salary.app.presentation.ui.history

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.moa.salary.app.core.extensions.convertMinutesToRoundedHours
import com.moa.salary.app.core.extensions.formatCurrency
import com.moa.salary.app.core.model.work.LocalDateModel
import com.moa.salary.app.core.model.work.MonthlyWorkSummary
import com.moa.salary.app.core.model.work.Schedule
import com.moa.salary.app.core.model.work.ScheduleType
import com.moa.salary.app.core.model.onboarding.Time
import com.moa.salary.app.presentation.R
import com.moa.salary.app.presentation.designsystem.component.CalendarHeader
import com.moa.salary.app.presentation.designsystem.component.Day
import com.moa.salary.app.presentation.designsystem.component.MoaTopAppBar
import com.moa.salary.app.presentation.designsystem.theme.MoaTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.time.DayOfWeek
import java.time.YearMonth
import java.util.Locale

@Composable
fun HistoryScreen(viewModel: HistoryViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedDateSchedules = remember(
        uiState.selectedDate,
        uiState.selectedWorkday,
        uiState.paydayDay,
    ) {
        viewModel.getSchedulesForSelectedDate()
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refresh()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    HistoryScreen(
        uiState = uiState,
        selectedDateSchedules = selectedDateSchedules,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun HistoryScreen(
    uiState: HistoryUiState,
    selectedDateSchedules: ImmutableList<Schedule>,
    onIntent: (HistoryIntent) -> Unit,
) {
    Scaffold(
        topBar = {
            MoaTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onIntent(HistoryIntent.ClickBack) }) {
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
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            item {
                HistoryCalendar(
                    uiState = uiState,
                    onIntent = onIntent,
                )
            }

            item {
                Spacer(Modifier.height(MoaTheme.spacing.spacing28))

                Text(
                    modifier = Modifier.padding(horizontal = MoaTheme.spacing.spacing16),
                    text = "${uiState.selectedDate.year}.${uiState.selectedDate.month}.${uiState.selectedDate.day}",
                    style = MoaTheme.typography.b1_500,
                    color = MoaTheme.colors.textLowEmphasis,
                )

                Spacer(Modifier.height(MoaTheme.spacing.spacing20))
            }

            items(selectedDateSchedules) { schedule ->
                ScheduleItem(
                    schedule = schedule,
                    onClick = { onIntent(HistoryIntent.ClickSchedule(schedule)) },
                )

                Spacer(Modifier.height(MoaTheme.spacing.spacing12))
            }

            if (selectedDateSchedules.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = MoaTheme.spacing.spacing32,
                                horizontal = MoaTheme.spacing.spacing16,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = stringResource(R.string.history_no_schedule),
                            style = MoaTheme.typography.b1_500,
                            color = MoaTheme.colors.textLowEmphasis,
                        )
                    }
                }
            }

            item {
                Spacer(Modifier.height(MoaTheme.spacing.spacing24))
            }
        }
    }
}

@Composable
private fun HistoryCalendar(
    uiState: HistoryUiState,
    onIntent: (HistoryIntent) -> Unit,

    ) {
    val currentMonth = YearMonth.of(uiState.currentYear, uiState.currentMonth)
    val startMonth = currentMonth.minusMonths(100)
    val endMonth = currentMonth.plusMonths(100)
    val daysOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.SUNDAY)

    val calendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = DayOfWeek.SUNDAY,
    )

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(calendarState.firstVisibleMonth) {
        val visibleMonth = calendarState.firstVisibleMonth.yearMonth
        if (visibleMonth.year != uiState.currentYear || visibleMonth.monthValue != uiState.currentMonth) {
            onIntent(HistoryIntent.SetMonth(visibleMonth.year, visibleMonth.monthValue))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
            .background(MoaTheme.colors.containerPrimary)
            .padding(horizontal = MoaTheme.spacing.spacing16)
    ) {
        MonthNavigator(
            month = uiState.currentMonth,
            onPreviousClick = {
                coroutineScope.launch {
                    calendarState.animateScrollToMonth(currentMonth.minusMonths(1))
                }
            },
            onNextClick = {
                coroutineScope.launch {
                    calendarState.animateScrollToMonth(currentMonth.plusMonths(1))
                }
            },
            onAddClick = { onIntent(HistoryIntent.ClickAddSchedule) },
        )

        Spacer(Modifier.height(MoaTheme.spacing.spacing16))

        WorkSummaryCard(summary = uiState.monthlyWorkSummary)

        Spacer(Modifier.height(MoaTheme.spacing.spacing32))

        CalendarHeader(daysOfWeek = daysOfWeek)

        HorizontalCalendar(
            modifier = Modifier.fillMaxWidth(),
            state = calendarState,
            dayContent = { day ->
                val calendarDay = uiState.calendarDays.find {
                    it.date?.year == day.date.year &&
                            it.date.month == day.date.monthValue &&
                            it.date.day == day.date.dayOfMonth
                }
                Day(
                    day = day,
                    calendarDay = calendarDay,
                    isSelected = uiState.selectedDate.year == day.date.year &&
                            uiState.selectedDate.month == day.date.monthValue &&
                            uiState.selectedDate.day == day.date.dayOfMonth,
                    onClick = {
                        if (day.position == DayPosition.MonthDate) {
                            onIntent(
                                HistoryIntent.ClickDate(
                                    LocalDateModel(
                                        day.date.year,
                                        day.date.monthValue,
                                        day.date.dayOfMonth,
                                    )
                                )
                            )
                        }
                    },
                )
            },
        )
    }
}

@Composable
private fun MonthNavigator(
    month: Int,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onAddClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = onPreviousClick)
                .padding(
                    start = 0.dp,
                    top = MoaTheme.spacing.spacing8,
                    end = MoaTheme.spacing.spacing8,
                    bottom = MoaTheme.spacing.spacing8,
                ),
            painter = painterResource(R.drawable.ic_24_chevron_left),
            contentDescription = stringResource(R.string.history_previous_month_description),
            tint = MoaTheme.colors.textHighEmphasis,
        )

        Text(
            text = stringResource(R.string.history_month_format, month),
            style = MoaTheme.typography.t2_500,
            color = MoaTheme.colors.textHighEmphasis,
        )

        Icon(
            modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = onNextClick)
                .padding(MoaTheme.spacing.spacing8),
            painter = painterResource(R.drawable.ic_24_chevron_right),
            contentDescription = stringResource(R.string.history_next_month_description),
            tint = MoaTheme.colors.textHighEmphasis,
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = onAddClick) {
            Icon(
                painter = painterResource(R.drawable.ic_plus),
                contentDescription = stringResource(R.string.history_add_schedule_description),
                tint = Color.Unspecified,
            )
        }
    }
}

@Composable
private fun WorkSummaryCard(summary: MonthlyWorkSummary) {
    val workedTimeText = summary.workedMinutes.convertMinutesToRoundedHours().toString()
    val standardTimeText = "${summary.standardMinutes.convertMinutesToRoundedHours()}시간"

    val workedSalaryText = "${summary.workedEarnings / 10000}"
    val standardSalaryText = "${summary.standardSalary / 10000}만원"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MoaTheme.radius.radius16))
            .background(MoaTheme.colors.containerSecondary)
            .padding(MoaTheme.spacing.spacing16),
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
                    text = workedTimeText,
                    style = MoaTheme.typography.b1_600,
                    color = MoaTheme.colors.textGreen,
                )
                Text(
                    text = " / $standardTimeText",
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
                    text = workedSalaryText,
                    style = MoaTheme.typography.b1_600,
                    color = MoaTheme.colors.textGreen,
                )
                Text(
                    text = " / $standardSalaryText",
                    style = MoaTheme.typography.b1_400,
                    color = MoaTheme.colors.textMediumEmphasis,
                )
            }
        }
    }
}

@Composable
private fun ScheduleItem(
    schedule: Schedule,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MoaTheme.spacing.spacing16)
            .clip(RoundedCornerShape(MoaTheme.radius.radius12))
            .background(MoaTheme.colors.containerSecondary)
            .clickable(onClick = onClick)
            .padding(MoaTheme.spacing.spacing16),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ScheduleIcon(type = schedule.type)

        Spacer(Modifier.width(MoaTheme.spacing.spacing12))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = when (schedule.type) {
                    ScheduleType.WORK_SCHEDULED -> stringResource(R.string.history_schedule_work_scheduled)
                    ScheduleType.WORK_COMPLETED -> stringResource(R.string.history_schedule_work_completed)
                    ScheduleType.VACATION -> stringResource(R.string.history_schedule_vacation)
                    ScheduleType.PAYDAY -> stringResource(
                        R.string.history_schedule_payday,
                        schedule.date.day
                    )
                },
                style = MoaTheme.typography.b2_500,
                color = MoaTheme.colors.textLowEmphasis,
            )

            Spacer(Modifier.height(2.dp))

            Text(
                text = when (schedule.type) {
                    ScheduleType.WORK_SCHEDULED,
                    ScheduleType.WORK_COMPLETED -> schedule.time?.getFormattedTimeRange() ?: ""

                    ScheduleType.VACATION -> schedule.time?.getFormattedTimeRange() ?: ""
                    ScheduleType.PAYDAY -> "+ ${formatCurrency(schedule.amount ?: 0)}원"
                },
                style = MoaTheme.typography.t2_700,
                color = MoaTheme.colors.textHighEmphasis,
            )
        }

        Icon(
            painter = painterResource(R.drawable.ic_24_chevron_right),
            contentDescription = stringResource(R.string.history_schedule_detail_description),
            tint = MoaTheme.colors.textLowEmphasis,
        )
    }
}

@Composable
private fun ScheduleIcon(type: ScheduleType) {
    val iconRes = when (type) {
        ScheduleType.WORK_SCHEDULED -> R.drawable.ic_24_working_yet
        ScheduleType.WORK_COMPLETED -> R.drawable.ic_24_working_done
        ScheduleType.VACATION -> R.drawable.ic_24_vacation
        ScheduleType.PAYDAY -> R.drawable.ic_24_salary_day
    }

    Icon(
        painter = painterResource(iconRes),
        contentDescription = null,
        tint = Color.Unspecified,
        modifier = Modifier.size(48.dp),
    )
}

sealed interface HistoryIntent {
    data object ClickBack : HistoryIntent
    data object ClickPreviousMonth : HistoryIntent
    data object ClickNextMonth : HistoryIntent
    data object ClickAddSchedule : HistoryIntent
    data class ClickDate(val date: LocalDateModel) : HistoryIntent
    data class ClickSchedule(val schedule: Schedule) : HistoryIntent
    data class SetMonth(val year: Int, val month: Int) : HistoryIntent
}

@Preview
@Composable
private fun HistoryScreenPreview() {
    MoaTheme {
        HistoryScreen(
            uiState = HistoryUiState(
                currentMonth = 1,
                monthlyWorkSummary = MonthlyWorkSummary(
                    workedMinutes = 7200,
                    standardMinutes = 9000,
                    workedEarnings = 3000000,
                    standardSalary = 4000000,
                ),
            ),
            selectedDateSchedules = persistentListOf(
                Schedule(
                    id = 1,
                    date = LocalDateModel(2026, 1, 14),
                    type = ScheduleType.WORK_SCHEDULED,
                    time = Time(8, 0, 20, 0),
                ),
            ),
            onIntent = {},
        )
    }
}