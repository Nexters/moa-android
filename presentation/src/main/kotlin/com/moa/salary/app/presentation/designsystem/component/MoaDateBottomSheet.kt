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
    var date by remember(initialDate) { mutableStateOf(initialDate) }
    var yearMonth by remember(initialDate) { mutableStateOf(initialDate.yearMonth) }

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

            Spacer(Modifier.height(MoaTheme.spacing.spacing32))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                MoaMonthNavigator(
                    month = yearMonth.monthValue,
                    previousEnabled = yearMonth.isAfter(joinedAt.yearMonth),
                    nextEnabled = yearMonth.isBefore(YearMonth.now().plusMonths(12)),
                    onPreviousClick = { yearMonth = yearMonth.minusMonths(1) },
                    onNextClick = { yearMonth = yearMonth.plusMonths(1) },
                )
            }

            Spacer(Modifier.height(MoaTheme.spacing.spacing12))

            MoaCalendar(
                joinedAt = joinedAt,
                selectedDate = date,
                selectedYearMonth = yearMonth,
                schedules = persistentMapOf(),
                outDateStyle = OutDateStyle.EndOfGrid,
                onScrollYearMonth = { yearMonth = it },
                onClickDate = { date = it }
            )

            MoaPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = MoaTheme.spacing.spacing20,
                        bottom = MoaTheme.spacing.spacing24,
                    ),
                onClick = { onConfirm(date) },
            ) {
                Text(
                    text = "확인",
                    style = MoaTheme.typography.t3_700,
                )
            }
        }
    }
}