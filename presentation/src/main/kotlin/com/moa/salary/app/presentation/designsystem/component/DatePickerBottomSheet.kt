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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.moa.salary.app.presentation.R
import com.moa.salary.app.presentation.designsystem.theme.Green40Main
import com.moa.salary.app.presentation.designsystem.theme.MoaTheme
import com.moa.salary.app.presentation.extensions.toFixedSize
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.YearMonth

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DatePickerBottomSheet(
//    currentDate: LocalDateModel,
//    onConfirm: (LocalDateModel) -> Unit,
//    onDismissRequest: () -> Unit,
//) {
//    val currentMonth by remember {
//        mutableStateOf(
//            YearMonth.of(
//                currentDate.year,
//                currentDate.month
//            )
//        )
//    }
//    val startMonth = currentMonth.minusMonths(100)
//    val endMonth = currentMonth.plusMonths(100)
//    var selectedDate by remember { mutableStateOf(currentDate) }
//    val scope = rememberCoroutineScope()
//
//    val calendarState = rememberCalendarState(
//        startMonth = startMonth,
//        endMonth = endMonth,
//        firstVisibleMonth = currentMonth,
//        firstDayOfWeek = DayOfWeek.SUNDAY,
//        outDateStyle = OutDateStyle.EndOfGrid,
//    )
//
//    MoaBottomSheet(onDismissRequest = onDismissRequest) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = MoaTheme.spacing.spacing20),
//        ) {
//            Text(
//                text = "날짜를 선택해주세요",
//                style = MoaTheme.typography.t1_700,
//                color = MoaTheme.colors.textHighEmphasis,
//            )
//
//            Spacer(Modifier.height(MoaTheme.spacing.spacing16))
//
//            MonthNavigator(
//                currentMonth = calendarState.firstVisibleMonth.yearMonth.monthValue,
//                onPreviousMonth = {
//                    scope.launch {
//                        calendarState.animateScrollToMonth(
//                            calendarState.firstVisibleMonth.yearMonth.minusMonths(
//                                1
//                            )
//                        )
//                    }
//                },
//                onNextMonth = {
//                    scope.launch {
//                        calendarState.animateScrollToMonth(
//                            calendarState.firstVisibleMonth.yearMonth.plusMonths(
//                                1
//                            )
//                        )
//                    }
//                },
//            )
//
//            Spacer(Modifier.height(MoaTheme.spacing.spacing8))
//
//            HorizontalCalendar(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = MoaTheme.spacing.spacing8),
//                state = calendarState,
//                dayContent = { day ->
//                    DatePickerDay(
//                        day = day,
//                        isSelected = selectedDate.year == day.date.year && selectedDate.month == day.date.monthValue && selectedDate.day == day.date.dayOfMonth,
//                        onDateClick = {
//                            selectedDate =
//                                LocalDateModel(it.date.year, it.date.monthValue, it.date.dayOfMonth)
//                        },
//                    )
//                },
//            )
//
//            MoaPrimaryButton(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(
//                        top = MoaTheme.spacing.spacing20,
//                        bottom = MoaTheme.spacing.spacing24,
//                    ),
//                onClick = {
//                    onConfirm(selectedDate)
//                },
//            ) {
//                Text(
//                    text = "확인",
//                    style = MoaTheme.typography.t3_700,
//                )
//            }
//        }
//    }
//}
//
//@Composable
//private fun MonthNavigator(
//    currentMonth: Int,
//    onPreviousMonth: () -> Unit,
//    onNextMonth: () -> Unit,
//) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically,
//    ) {
//        IconButton(onClick = onPreviousMonth) {
//            Icon(
//                painter = painterResource(R.drawable.ic_24_chevron_left),
//                contentDescription = "Previous Month",
//                tint = MoaTheme.colors.textHighEmphasis,
//            )
//        }
//
//        Text(
//            text = "${currentMonth}월",
//            style = MoaTheme.typography.t2_700,
//            color = MoaTheme.colors.textHighEmphasis,
//        )
//
//        IconButton(onClick = onNextMonth) {
//            Icon(
//                painter = painterResource(R.drawable.ic_24_chevron_right),
//                contentDescription = "Next Month",
//                tint = MoaTheme.colors.textHighEmphasis,
//            )
//        }
//    }
//}
//
//@Composable
//private fun DatePickerDay(
//    day: CalendarDay,
//    isSelected: Boolean,
//    onDateClick: (CalendarDay) -> Unit,
//) {
//    Column(
//        modifier = Modifier
//            .padding(
//                top = 2.dp,
//                bottom = 32.dp,
//                start = 4.dp,
//                end = 4.dp,
//            ),
//        horizontalAlignment = Alignment.CenterHorizontally,
//    ) {
//        Box(
//            modifier = Modifier
//                .size(28.dp)
//                .clip(CircleShape)
//                .then(
//                    if (isSelected && day.position == DayPosition.MonthDate) {
//                        Modifier.background(Green40Main)
//                    } else {
//                        Modifier
//                    }
//                )
//                .clickable { onDateClick(day) },
//            contentAlignment = Alignment.Center,
//        ) {
//            Text(
//                text = day.date.dayOfMonth.toString(),
//                style = MoaTheme.typography.b1_500.toFixedSize(),
//                color = when {
//                    day.position != DayPosition.MonthDate -> Color.Transparent
//                    isSelected -> MoaTheme.colors.textHighEmphasisReverse
//                    else -> MoaTheme.colors.textHighEmphasis
//                },
//            )
//        }
//    }
//}