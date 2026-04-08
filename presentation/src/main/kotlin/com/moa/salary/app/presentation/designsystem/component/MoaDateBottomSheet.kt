package com.moa.salary.app.presentation.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.yearMonth
import com.moa.salary.app.presentation.designsystem.theme.MoaTheme
import kotlinx.collections.immutable.persistentMapOf
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoaDateBottomSheet(
    joinedAt: LocalDate,
    initialDate: LocalDate,
    onConfirm: (LocalDate) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var currentDate by remember(initialDate) { mutableStateOf(initialDate) }

    MoaBottomSheet(onDismissRequest = onDismissRequest) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.padding(start = MoaTheme.spacing.spacing20),
                text = "날짜를 선택해주세요",
                style = MoaTheme.typography.t1_700,
                color = MoaTheme.colors.textHighEmphasis,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing32))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                MoaMonthNavigator(
                    month = currentDate.monthValue,
                    previousEnabled = currentDate.yearMonth.isAfter(joinedAt.yearMonth),
                    nextEnabled = currentDate.yearMonth.isBefore(YearMonth.now().plusMonths(12)),
                    onPreviousClick = { currentDate = currentDate.minusMonths(1) },
                    onNextClick = { currentDate = currentDate.plusMonths(1) },
                )
            }

            Spacer(Modifier.height(MoaTheme.spacing.spacing12))

            MoaCalendar(
                joinedAt = joinedAt,
                selectedDate = currentDate,
                selectedYearMonth = currentDate.yearMonth,
                workdays = persistentMapOf(),
                outDateStyle = OutDateStyle.EndOfGrid,
                onScrollYearMonth = { newYearMonth ->
                    val currentYearMonth = currentDate.yearMonth

                    if (newYearMonth != currentYearMonth) {
                        val newDate = if (newYearMonth.isAfter(currentYearMonth)) {
                            currentDate.plusMonths(1)
                        } else {
                            currentDate.minusMonths(1)
                        }
                        currentDate = newDate
                    }
                },
                onClickDate = { currentDate = it }
            )

            MoaPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = MoaTheme.spacing.spacing20,
                        end = MoaTheme.spacing.spacing20,
                        top = MoaTheme.spacing.spacing20,
                        bottom = MoaTheme.spacing.spacing24,
                    ),
                onClick = {
                    onDismissRequest()
                    onConfirm(currentDate)
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