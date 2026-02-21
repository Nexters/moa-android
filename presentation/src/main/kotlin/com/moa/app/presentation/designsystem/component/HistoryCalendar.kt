package com.moa.app.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.moa.app.presentation.R
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.moa.app.presentation.designsystem.theme.Green40Main
import com.moa.app.presentation.designsystem.theme.MoaTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarHeader(daysOfWeek: List<DayOfWeek>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        daysOfWeek.forEach { dayOfWeek ->
            Text(
                modifier = Modifier.weight(1f),
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN),
                style = MoaTheme.typography.b2_400,
                color = MoaTheme.colors.textLowEmphasis,
                textAlign = TextAlign.Center,
            )
        }
    }
    Spacer(Modifier.height(MoaTheme.spacing.spacing8))
}

@Composable
fun Day(
    day: CalendarDay,
    calendarDay: com.moa.app.presentation.ui.history.CalendarDay?,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val today = LocalDate.now()
    val isToday = day.date == today
    val isCurrentMonth = day.position == DayPosition.MonthDate
    val isFutureDate = day.date.isAfter(today)

    if (!isCurrentMonth) {
        Box(modifier = Modifier.aspectRatio(0.7f))
        return
    }

    Column(
        modifier = Modifier
            .aspectRatio(0.7f)
            .padding(horizontal = 4.dp)
            .padding(top = 2.dp, bottom = 24.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .then(
                    if (isSelected) {
                        Modifier
                            .clip(CircleShape)
                            .background(MoaTheme.colors.containerSecondary)
                    } else {
                        Modifier
                    }
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = day.date.dayOfMonth.toString(),
                style = MoaTheme.typography.b1_400,
                color = when {
                    isToday -> MoaTheme.colors.textGreen
                    else -> MoaTheme.colors.textHighEmphasis
                },
            )
        }

        if (calendarDay?.hasPayday == true || calendarDay?.hasVacation == true) {
            Spacer(Modifier.height(2.dp))
        }

        when {
            calendarDay?.hasPayday == true && (calendarDay.hasWorkScheduled || calendarDay.hasWorkCompleted) -> {
                Text(
                    text = stringResource(R.string.history_calendar_payday),
                    style = MoaTheme.typography.c1_400,
                    color = MoaTheme.colors.textGreen,
                    maxLines = 1,
                )
            }
            calendarDay?.hasPayday == true && calendarDay.hasVacation -> {
                Row(modifier = Modifier.wrapContentWidth(unbounded = true)) {
                    Text(
                        text = stringResource(R.string.history_calendar_payday),
                        style = MoaTheme.typography.c1_400,
                        color = MoaTheme.colors.textGreen,
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text = stringResource(R.string.history_calendar_separator),
                        style = MoaTheme.typography.c1_400,
                        color = MoaTheme.colors.textLowEmphasis,
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text = stringResource(R.string.history_schedule_vacation),
                        style = MoaTheme.typography.c1_400,
                        color = MoaTheme.colors.textMediumEmphasis,
                    )
                }
            }
            calendarDay?.hasPayday == true -> {
                Text(
                    text = stringResource(R.string.history_calendar_payday),
                    style = MoaTheme.typography.c1_400,
                    color = MoaTheme.colors.textGreen,
                    maxLines = 1,
                )
            }
            calendarDay?.hasVacation == true -> {
                Text(
                    text = stringResource(R.string.history_schedule_vacation),
                    style = MoaTheme.typography.c1_400,
                    color = MoaTheme.colors.textMediumEmphasis,
                    maxLines = 1,
                )
            }
        }

        Spacer(Modifier.height(6.dp))

        when {
            calendarDay?.hasWorkScheduled == true && isFutureDate -> {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(MoaTheme.colors.textLowEmphasis),
                )
            }
            calendarDay?.hasWorkCompleted == true -> {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Green40Main),
                )
            }
            calendarDay?.hasWorkScheduled == true -> {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Green40Main),
                )
            }
        }
    }
}