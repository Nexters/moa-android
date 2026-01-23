package com.moa.app.presentation.ui.onboarding.workschedule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moa.app.core.makeTimeString
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.component.MoaPrimaryButton
import com.moa.app.presentation.designsystem.component.MoaTermBottomSheet
import com.moa.app.presentation.designsystem.component.MoaTimeBottomSheet
import com.moa.app.presentation.designsystem.component.MoaTopAppBar
import com.moa.app.presentation.designsystem.theme.MoaTheme
import com.moa.app.presentation.model.Term
import com.moa.app.presentation.model.Time
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf

@Composable
fun WorkScheduleScreen(viewModel: WorkScheduleViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WorkScheduleScreen(
        uiState = uiState,
        onIntent = viewModel::onIntent,
    )

    uiState.showTimeBottomSheet?.let {
        MoaTimeBottomSheet(
            time = it,
            onClickButton = { startHour, startMinute, endHour, endMinute ->
                val updateTime = when (it) {
                    is Time.Work -> Time.Work(
                        startHour = startHour,
                        startMinute = startMinute,
                        endHour = endHour,
                        endMinute = endMinute,
                    )

                    is Time.Lunch -> Time.Lunch(
                        startHour = startHour,
                        startMinute = startMinute,
                        endHour = endHour,
                        endMinute = endMinute,
                    )

                    else -> return@MoaTimeBottomSheet
                }
                viewModel.onIntent(WorkScheduleIntent.SetTime(updateTime))
            },
            onDismissRequest = { viewModel.onIntent(WorkScheduleIntent.ShowTimeBottomSheet(null)) }
        )
    }

    if (uiState.showTermBottomSheet) {
        MoaTermBottomSheet(
            terms = uiState.terms,
            onClickTerm = { viewModel.onIntent(WorkScheduleIntent.ClickTerm(it)) },
            onClickArrow = { viewModel.onIntent(WorkScheduleIntent.ClickArrow(it)) },
            onClickButton = {
                viewModel.onIntent(WorkScheduleIntent.ShowTermBottomSheet(false))
                viewModel.onIntent(WorkScheduleIntent.ClickNext)
            },
        )
    }
}

@Composable
private fun WorkScheduleScreen(
    uiState: WorkScheduleUiState,
    onIntent: (WorkScheduleIntent) -> Unit,
) {
    Scaffold(
        topBar = {
            MoaTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onIntent(WorkScheduleIntent.ClickBack) }) {
                        Icon(
                            painter = painterResource(R.drawable.icon_back),
                            contentDescription = "Back",
                            tint = MoaTheme.colors.textHighEmphasis,
                        )
                    }
                }
            )
        },
        containerColor = MoaTheme.colors.bgPrimary,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = MoaTheme.spacing.spacing20),
        ) {
            Spacer(Modifier.height(MoaTheme.spacing.spacing20))

            Text(
                text = "언제 근무하나요?",
                color = MoaTheme.colors.textHighEmphasis,
                style = MoaTheme.typography.t1_700,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing32))

            Text(
                text = "근무 요일",
                color = MoaTheme.colors.textMediumEmphasis,
                style = MoaTheme.typography.b2_500,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing8))

            WorkScheduleDays(
                selectedDays = uiState.selectedWorkScheduleDays,
                onIntent = onIntent,
            )

            WorkScheduleTimes(
                times = uiState.times,
                onIntent = onIntent,
            )

            Spacer(Modifier.weight(1f))

            MoaPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = MoaTheme.spacing.spacing24)
                    .height(64.dp),
                enabled = uiState.selectedWorkScheduleDays.isNotEmpty(),
                onClick = { onIntent(WorkScheduleIntent.ShowTermBottomSheet(true)) },
            ) {
                Text(
                    text = "다음",
                    style = MoaTheme.typography.t3_700,
                )
            }
        }
    }
}

@Composable
private fun WorkScheduleDays(
    selectedDays: ImmutableSet<WorkScheduleDay>,
    onIntent: (WorkScheduleIntent) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MoaTheme.spacing.spacing8)
    ) {
        WorkScheduleDay.entries.forEach { workScheduleDay ->
            WorkScheduleDay(
                day = workScheduleDay.title,
                selected = selectedDays.contains(workScheduleDay),
                onClick = {
                    onIntent(WorkScheduleIntent.ClickWorkScheduleDay(workScheduleDay))
                }
            )
        }
    }
}

@Composable
private fun RowScope.WorkScheduleDay(
    day: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Text(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(MoaTheme.radius.radius8))
            .background(
                color = if (selected) {
                    MoaTheme.colors.textHighEmphasis
                } else {
                    MoaTheme.colors.containerPrimary
                },
                shape = RoundedCornerShape(MoaTheme.radius.radius8)
            )
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        text = day,
        color = if (selected) {
            MoaTheme.colors.textHighEmphasisReverse
        } else {
            MoaTheme.colors.textDisabled
        },
        style = if (selected) {
            MoaTheme.typography.b1_600
        } else {
            MoaTheme.typography.b1_500
        },
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun WorkScheduleTimes(
    times: ImmutableList<Time>,
    onIntent: (WorkScheduleIntent) -> Unit,
) {
    times.forEach { time ->
        Spacer(Modifier.height(MoaTheme.spacing.spacing24))

        Text(
            text = time.title,
            color = MoaTheme.colors.textMediumEmphasis,
            style = MoaTheme.typography.b2_500,
        )

        Spacer(Modifier.height(MoaTheme.spacing.spacing8))

        WorkScheduleTime(
            startTime = makeTimeString(time.startHour, time.startMinute),
            endTime = makeTimeString(time.endHour, time.endMinute),
            onClick = { onIntent(WorkScheduleIntent.ShowTimeBottomSheet(time)) }
        )
    }
}

@Composable
private fun WorkScheduleTime(
    startTime: String,
    endTime: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MoaTheme.radius.radius12))
            .background(
                color = MoaTheme.colors.containerPrimary,
                shape = RoundedCornerShape(MoaTheme.radius.radius12),
            )
            .clickable { onClick() }
            .padding(
                vertical = MoaTheme.spacing.spacing16,
                horizontal = MoaTheme.spacing.spacing20,
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = startTime,
            color = MoaTheme.colors.textHighEmphasis,
            style = MoaTheme.typography.t2_700,
            textAlign = TextAlign.Center,
        )

        Icon(
            modifier = Modifier.padding(horizontal = 10.dp),
            painter = painterResource(R.drawable.icon_arrow_right),
            contentDescription = "ArrowRight",
            tint = MoaTheme.colors.textLowEmphasis,
        )

        Text(
            modifier = Modifier.weight(1f),
            text = endTime,
            color = MoaTheme.colors.textHighEmphasis,
            style = MoaTheme.typography.t2_700,
            textAlign = TextAlign.Center,
        )
    }
}

sealed interface WorkScheduleIntent {
    data object ClickBack : WorkScheduleIntent

    @JvmInline
    value class ClickWorkScheduleDay(val day: WorkScheduleDay) : WorkScheduleIntent

    @JvmInline
    value class ShowTimeBottomSheet(val time: Time?) : WorkScheduleIntent

    @JvmInline
    value class SetTime(val time: Time) : WorkScheduleIntent

    @JvmInline
    value class ShowTermBottomSheet(val visible: Boolean) : WorkScheduleIntent

    @JvmInline
    value class ClickTerm(val term: Term) : WorkScheduleIntent

    @JvmInline
    value class ClickArrow(val url: String) : WorkScheduleIntent

    data object ClickNext : WorkScheduleIntent
}

@Preview
@Composable
private fun WorkScheduleScreenPreview() {
    MoaTheme {
        WorkScheduleScreen(
            uiState = WorkScheduleUiState(
                selectedWorkScheduleDays = persistentSetOf(
                    WorkScheduleDay.MONDAY,
                    WorkScheduleDay.WEDNESDAY,
                    WorkScheduleDay.FRIDAY,
                ),
            ),
            onIntent = {},
        )
    }
}