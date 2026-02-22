package com.moa.salary.app.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moa.salary.app.core.model.history.LocalDateModel
import com.moa.salary.app.presentation.R
import com.moa.salary.app.presentation.designsystem.theme.Green40Main
import com.moa.salary.app.presentation.designsystem.theme.MoaTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerBottomSheet(
    selectedDate: LocalDateModel,
    onConfirm: (LocalDateModel) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var currentYear by remember { mutableIntStateOf(selectedDate.year) }
    var currentMonth by remember { mutableIntStateOf(selectedDate.month) }
    var tempSelectedDate by remember { mutableStateOf(selectedDate) }

    MoaBottomSheet(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MoaTheme.spacing.spacing20),
        ) {
            Text(
                text = "날짜를 선택해주세요",
                style = MoaTheme.typography.t1_700,
                color = MoaTheme.colors.textHighEmphasis,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing16))

            MonthNavigator(
                currentMonth = currentMonth,
                onPreviousMonth = {
                    val yearMonth = YearMonth.of(currentYear, currentMonth).minusMonths(1)
                    currentYear = yearMonth.year
                    currentMonth = yearMonth.monthValue
                },
                onNextMonth = {
                    val yearMonth = YearMonth.of(currentYear, currentMonth).plusMonths(1)
                    currentYear = yearMonth.year
                    currentMonth = yearMonth.monthValue
                },
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing8))

            // Calendar Header
            CalendarHeader()

            // Calendar Grid
            CalendarGridForPicker(
                year = currentYear,
                month = currentMonth,
                selectedDate = tempSelectedDate,
                onDateClick = { date ->
                    tempSelectedDate = date
                },
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing20))

            MoaPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = MoaTheme.spacing.spacing20,
                        bottom = MoaTheme.spacing.spacing24,
                    )
                    .height(64.dp),
                onClick = {
                    onConfirm(tempSelectedDate)
                },
            ) {
                Text(
                    text = "확인",
                    style = MoaTheme.typography.t3_700,
                )
            }
        }
    }
}

@Composable
private fun MonthNavigator(
    currentMonth: Int,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(
                painter = painterResource(R.drawable.ic_24_chevron_left),
                contentDescription = "Previous Month",
                tint = MoaTheme.colors.textHighEmphasis,
            )
        }

        Text(
            text = "${currentMonth}월",
            style = MoaTheme.typography.t2_700,
            color = MoaTheme.colors.textHighEmphasis,
        )

        IconButton(onClick = onNextMonth) {
            Icon(
                painter = painterResource(R.drawable.ic_24_chevron_right),
                contentDescription = "Next Month",
                tint = MoaTheme.colors.textHighEmphasis,
            )
        }
    }
}

@Composable
private fun CalendarHeader() {
    val daysOfWeek = listOf(DayOfWeek.SUNDAY) + DayOfWeek.entries.dropLast(1)
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        daysOfWeek.forEach { day ->
            Text(
                modifier = Modifier.weight(1f),
                text = day.getDisplayName(TextStyle.SHORT, Locale.KOREAN),
                style = MoaTheme.typography.b2_500,
                color = MoaTheme.colors.textLowEmphasis,
                textAlign = TextAlign.Center,
            )
        }
    }
    Spacer(Modifier.height(MoaTheme.spacing.spacing8))
}

@Composable
private fun CalendarGridForPicker(
    year: Int,
    month: Int,
    selectedDate: LocalDateModel,
    onDateClick: (LocalDateModel) -> Unit,
) {
    val yearMonth = YearMonth.of(year, month)
    val firstDayOfMonth = yearMonth.atDay(1)
    val daysInMonth = yearMonth.lengthOfMonth()
    val startDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
    val today = LocalDate.now()

    val calendarDays = mutableListOf<LocalDateModel?>()

    repeat(startDayOfWeek) {
        calendarDays.add(null)
    }

    for (day in 1..daysInMonth) {
        calendarDays.add(LocalDateModel(year, month, day))
    }

    val rowCount = (calendarDays.size + 6) / 7

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier
            .fillMaxWidth()
            .height((rowCount * (44 + 38)).dp),
        userScrollEnabled = false,
    ) {
        items(calendarDays) { date ->
            Column(
                modifier = Modifier.padding(bottom = 38.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (date == null) {
                    Box(
                        modifier = Modifier.size(44.dp),
                    )
                } else {
                    val isSelected = date.year == selectedDate.year &&
                            date.month == selectedDate.month &&
                            date.day == selectedDate.day
                    val isToday = today.year == date.year &&
                            today.monthValue == date.month &&
                            today.dayOfMonth == date.day

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .then(
                                if (isSelected) {
                                    Modifier.background(Green40Main)
                                } else {
                                    Modifier
                                }
                            )
                            .clickable { onDateClick(date) },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = date.day.toString(),
                            style = MoaTheme.typography.b1_500,
                            color = when {
                                isSelected -> MoaTheme.colors.textHighEmphasisReverse
                                isToday -> MoaTheme.colors.textGreen
                                else -> MoaTheme.colors.textHighEmphasis
                            },
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun DatePickerBottomSheetPreview() {
    MoaTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MoaTheme.colors.bgPrimary)
                .padding(MoaTheme.spacing.spacing20),
        ) {
            Text(
                text = "날짜를 선택해주세요",
                style = MoaTheme.typography.t1_700,
                color = MoaTheme.colors.textHighEmphasis,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing16))

            MonthNavigator(
                currentMonth = 1,
                onPreviousMonth = {},
                onNextMonth = {},
            )

            CalendarHeader()

            CalendarGridForPicker(
                year = 2026,
                month = 1,
                selectedDate = LocalDateModel(2026, 1, 14),
                onDateClick = {},
            )

            MoaPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = MoaTheme.spacing.spacing20,
                        bottom = MoaTheme.spacing.spacing24,
                    )
                    .height(64.dp),
                onClick = {},
            ) {
                Text(
                    text = "확인",
                    style = MoaTheme.typography.t3_700,
                )
            }
        }
    }
}