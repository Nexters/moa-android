package com.moa.salary.app.presentation.designsystem.component

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import com.moa.salary.app.core.model.work.Event
import com.moa.salary.app.core.model.work.Workday
import com.moa.salary.app.core.model.work.WorkdayStatus
import com.moa.salary.app.core.model.work.WorkdayType
import com.moa.salary.app.presentation.R
import com.moa.salary.app.presentation.designsystem.theme.Green40Main
import com.moa.salary.app.presentation.designsystem.theme.MoaTheme
import com.moa.salary.app.presentation.extensions.toFixedSize
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun MoaCalendar(
    joinedAt: LocalDate,
    selectedDate: LocalDate,
    selectedYearMonth: YearMonth,
    workdays: ImmutableMap<LocalDate, Workday>,
    outDateStyle: OutDateStyle = OutDateStyle.EndOfRow,
    onClickDate: (LocalDate) -> Unit,
    onScrollYearMonth: (YearMonth) -> Unit,
) {
    val startMonth = remember(joinedAt) { YearMonth.from(joinedAt) }
    val endMonth = remember(startMonth) { selectedYearMonth.plusMonths(12) }

    val daysOfWeek = remember { daysOfWeek() }
    val calendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = selectedYearMonth,
        firstDayOfWeek = DayOfWeek.SUNDAY,
        outDateStyle = outDateStyle
    )

    LaunchedEffect(selectedYearMonth) {
        calendarState.animateScrollToMonth(selectedYearMonth)
    }

    LaunchedEffect(calendarState.firstVisibleMonth) {
        onScrollYearMonth(calendarState.firstVisibleMonth.yearMonth)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MoaTheme.spacing.spacing8)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            daysOfWeek.forEach { dayOfWeek ->
                Text(
                    modifier = Modifier.weight(1f),
                    text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN),
                    style = MoaTheme.typography.b2_400.toFixedSize(),
                    color = MoaTheme.colors.textLowEmphasis,
                    textAlign = TextAlign.Center,
                )
            }
        }

        HorizontalCalendar(
            modifier = Modifier.fillMaxWidth(),
            state = calendarState,
            dayContent = {
                Day(
                    isToday = it.date == LocalDate.now(),
                    isSelected = it.date == selectedDate,
                    workday = workdays[it.date],
                    day = it,
                    onClick = { onClickDate(it.date) },
                )
            },
        )
    }
}

@Composable
private fun Day(
    isToday: Boolean,
    isSelected: Boolean,
    workday: Workday?,
    day: CalendarDay,
    onClick: (CalendarDay) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(day) },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 2.dp,
                    horizontal = 4.dp,
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(28.dp)
                    .background(color = if (isSelected) MoaTheme.colors.containerSecondary else Color.Transparent)
            )

            Text(
                text = day.date.dayOfMonth.toString(),
                style = MoaTheme.typography.b1_400.toFixedSize(),
                color = when {
                    day.position != DayPosition.MonthDate -> Color.Transparent
                    isToday -> MoaTheme.colors.textGreen
                    else -> MoaTheme.colors.textHighEmphasis
                }
            )
        }

        DayBottomContent(
            status = workday?.status ?: WorkdayStatus.NONE,
            isPayday = workday?.events?.contains(Event.PAYDAY) ?: false,
            isVacation = workday?.type == WorkdayType.VACATION
        )
    }
}

@Composable
private fun DayBottomContent(
    status: WorkdayStatus,
    isPayday: Boolean,
    isVacation: Boolean,
) {
    if (isPayday && isVacation) {
        Row {
            Text(
                text = stringResource(R.string.history_calendar_payday),
                style = MoaTheme.typography.c1_400.toFixedSize(),
                color = MoaTheme.colors.textGreen,
            )
            Spacer(Modifier.width(2.dp))
            Text(
                text = stringResource(R.string.history_calendar_separator),
                style = MoaTheme.typography.c1_400.toFixedSize(),
                color = MoaTheme.colors.textLowEmphasis,
            )
            Spacer(Modifier.width(2.dp))
            Text(
                text = stringResource(R.string.history_schedule_vacation),
                style = MoaTheme.typography.c1_400.toFixedSize(),
                color = MoaTheme.colors.textMediumEmphasis,
            )
        }
    } else if (isPayday) {
        Text(
            text = stringResource(R.string.history_calendar_payday),
            style = MoaTheme.typography.c1_400.toFixedSize(),
            color = MoaTheme.colors.textGreen,
        )
    } else if (isVacation) {
        Text(
            text = stringResource(R.string.history_schedule_vacation),
            style = MoaTheme.typography.c1_400.toFixedSize(),
            color = MoaTheme.colors.textMediumEmphasis,
            maxLines = 1,
        )
    } else {
        Spacer(Modifier.height(4.dp))
        when (status) {
            WorkdayStatus.SCHEDULED -> {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(MoaTheme.colors.textLowEmphasis),
                )
            }

            WorkdayStatus.COMPLETED -> {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Green40Main),
                )
            }

            WorkdayStatus.NONE -> {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color.Transparent),
                )
            }
        }
        Spacer(Modifier.height(8.dp))
    }

    Spacer(Modifier.height(16.dp))
}

@Preview
@Composable
private fun MoaCalendarPreview() {
    MoaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MoaTheme.colors.bgSecondary)
        ) {
            MoaCalendar(
                joinedAt = LocalDate.now(),
                selectedDate = LocalDate.now().minusDays(1),
                selectedYearMonth = YearMonth.now(),
                workdays = persistentMapOf(),
                onScrollYearMonth = {},
                onClickDate = {},
            )
        }
    }
}