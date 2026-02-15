package com.moa.app.presentation.ui.home.working

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moa.app.core.model.onboarding.Time
import com.moa.app.presentation.R
import com.moa.app.presentation.navigation.HomeNavigation
import com.moa.app.presentation.designsystem.component.MoaScheduleAdjustBottomSheet
import com.moa.app.presentation.designsystem.component.MoaPrimaryButton
import com.moa.app.presentation.designsystem.component.MoaTertiaryButton
import com.moa.app.presentation.designsystem.component.MoaVacationButton
import com.moa.app.presentation.designsystem.component.MoaTimeBottomSheet
import com.moa.app.presentation.designsystem.component.MoaTooltipBanner
import com.moa.app.presentation.designsystem.component.ScheduleAdjustOption
import com.moa.app.presentation.designsystem.theme.MoaTheme
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.moa.app.presentation.ui.home.working.model.WorkStatus
import com.moa.app.presentation.ui.home.working.model.WorkingIntent
import com.moa.app.presentation.ui.home.working.model.WorkingUiState

@Composable
fun WorkingScreen(
    args: HomeNavigation.Working,
    viewModel: WorkingViewModel = hiltViewModel<WorkingViewModel, WorkingViewModel.Factory> { factory ->
        factory.create(args)
    },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WorkingScreen(
        uiState = uiState,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun WorkingScreen(
    uiState: WorkingUiState,
    onIntent: (WorkingIntent) -> Unit,
) {
    if (uiState.showScheduleAdjustBottomSheet) {
        MoaScheduleAdjustBottomSheet(
            onDismissRequest = { onIntent(WorkingIntent.DismissScheduleAdjustBottomSheet) },
            onConfirm = { option ->
                when (option) {
                    ScheduleAdjustOption.VACATION -> onIntent(WorkingIntent.SelectVacation)
                    ScheduleAdjustOption.END_WORK -> onIntent(WorkingIntent.SelectEndWork)
                    ScheduleAdjustOption.ADJUST_TIME -> onIntent(WorkingIntent.SelectAdjustTime)
                }
            },
        )
    }

    if (uiState.showTimeBottomSheet) {
        MoaTimeBottomSheet(
            time = Time(
                startHour = uiState.startHour,
                startMinute = uiState.startMinute,
                endHour = uiState.endHour,
                endMinute = uiState.endMinute,
            ),
            title = stringResource(R.string.working_time_bottom_sheet_title),
            onPositive = { time ->
                onIntent(
                    WorkingIntent.UpdateWorkTime(
                        startHour = time.startHour,
                        startMinute = time.startMinute,
                        endHour = time.endHour,
                        endMinute = time.endMinute,
                    )
                )
            },
            onDismissRequest = { onIntent(WorkingIntent.DismissTimeBottomSheet) },
        )
    }

    if (uiState.showMoreWorkBottomSheet) {
        MoaTimeBottomSheet(
            time = Time(
                startHour = uiState.startHour,
                startMinute = uiState.startMinute,
                endHour = uiState.endHour,
                endMinute = uiState.endMinute,
            ),
            title = stringResource(R.string.working_more_work_title),
            negativeText = stringResource(R.string.schedule_adjust_cancel),
            endTimeOnly = true,
            onNegative = { onIntent(WorkingIntent.DismissMoreWorkBottomSheet) },
            onPositive = { time ->
                onIntent(WorkingIntent.ConfirmMoreWork(time.endHour, time.endMinute))
            },
            onDismissRequest = { onIntent(WorkingIntent.DismissMoreWorkBottomSheet) },
        )
    }

    if (uiState.showWorkTimeEditBottomSheet) {
        MoaTimeBottomSheet(
            time = Time(
                startHour = uiState.startHour,
                startMinute = uiState.startMinute,
                endHour = uiState.endHour,
                endMinute = uiState.endMinute,
            ),
            title = stringResource(R.string.working_work_time_edit_title),
            negativeText = stringResource(R.string.working_today_vacation),
            endTimeOnly = true,
            onNegative = { onIntent(WorkingIntent.ClickTodayVacation) },
            onPositive = { time ->
                onIntent(WorkingIntent.ConfirmWorkTimeEdit(time.endHour, time.endMinute))
            },
            onDismissRequest = { onIntent(WorkingIntent.DismissWorkTimeEditBottomSheet) },
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MoaTheme.spacing.spacing20),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val animatedHeightFraction by animateFloatAsState(
            targetValue = uiState.coinHeightFraction,
            animationSpec = tween(durationMillis = 2000),
            label = "coinHeightAnimation",
        )

        BoxWithConstraints(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter,
        ) {
            val totalHeight = maxHeight
            val coinGraphHeight = totalHeight * animatedHeightFraction

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (!uiState.showWorkCompletionOverlay) {
                    RollingTooltipBanner(
                        monthSalary = uiState.monthSalary,
                        todaySalary = uiState.todaySalary,
                        remainingHours = uiState.remainingHours,
                        currentIndex = uiState.currentTooltipIndex,
                        isOnVacation = uiState.isOnVacation,
                    )

                    Spacer(Modifier.height(MoaTheme.spacing.spacing20))
                }

                TodaySalarySection(
                    todaySalary = uiState.todaySalaryDisplay,
                )

                Spacer(Modifier.height(22.dp))

                CoinGraph(
                    modifier = Modifier.height(coinGraphHeight),
                    isOnVacation = uiState.isOnVacation,
                )
            }
        }

        if (uiState.showWorkCompletionOverlay) {
            WorkCompletionSection(
                accumulatedSalary = uiState.monthSalary,
                workTimeDisplay = "${uiState.startTimeDisplay} - ${uiState.endTimeDisplay}",
                onContinueWorking = { onIntent(WorkingIntent.ClickContinueWorking) },
                onComplete = { onIntent(WorkingIntent.ClickCompleteWork) },
                onWorkTimeClick = { onIntent(WorkingIntent.ClickWorkTimeEdit) },
            )
        } else {
            WorkingStatusSection(
                workStatus = uiState.workStatus,
                elapsedTime = uiState.elapsedTimeDisplay,
                progress = uiState.progress,
                startTime = uiState.startTimeDisplay,
                endTime = uiState.endTimeDisplay,
                isOnVacation = uiState.isOnVacation,
                onAdjustScheduleClick = { onIntent(WorkingIntent.ClickAdjustSchedule) },
            )
        }

            Spacer(Modifier.height(MoaTheme.spacing.spacing24))
        }

        if (uiState.showConfetti) {
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.confeti)
            )
            val progress by animateLottieCompositionAsState(
                composition = composition,
                iterations = 1,
            )

            LaunchedEffect(progress) {
                if (progress == 1f) {
                    onIntent(WorkingIntent.DismissConfetti)
                }
            }

            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.fillMaxSize(),
            )
        }

    }
}

@Composable
private fun RollingTooltipBanner(
    monthSalary: String,
    todaySalary: Long,
    remainingHours: Int,
    currentIndex: Int,
    isOnVacation: Boolean,
) {
    val purchasableMessage = when {
        todaySalary >= 500000 -> stringResource(R.string.working_tooltip_purchasable_500000)
        todaySalary >= 450000 -> stringResource(R.string.working_tooltip_purchasable_450000)
        todaySalary >= 400000 -> stringResource(R.string.working_tooltip_purchasable_400000)
        todaySalary >= 350000 -> stringResource(R.string.working_tooltip_purchasable_350000)
        todaySalary >= 300000 -> stringResource(R.string.working_tooltip_purchasable_300000)
        todaySalary >= 250000 -> stringResource(R.string.working_tooltip_purchasable_250000)
        todaySalary >= 200000 -> stringResource(R.string.working_tooltip_purchasable_200000)
        todaySalary >= 100000 -> stringResource(R.string.working_tooltip_purchasable_100000)
        todaySalary >= 50000 -> stringResource(R.string.working_tooltip_purchasable_50000)
        todaySalary >= 30000 -> stringResource(R.string.working_tooltip_purchasable_30000)
        todaySalary >= 20000 -> stringResource(R.string.working_tooltip_purchasable_20000)
        todaySalary >= 10000 -> stringResource(R.string.working_tooltip_purchasable_10000)
        else -> stringResource(R.string.working_tooltip_purchasable_default)
    }

    val tooltipMessages = if (isOnVacation) {
        listOf(stringResource(R.string.working_tooltip_vacation))
    } else {
        listOf(
            stringResource(R.string.working_tooltip_month_salary, monthSalary),
            purchasableMessage,
            stringResource(R.string.working_tooltip_remaining_time, remainingHours),
        )
    }

    val displayIndex = if (isOnVacation) 0 else currentIndex % tooltipMessages.size

    MoaTooltipBanner {
        AnimatedContent(
            targetState = displayIndex,
            transitionSpec = {
                (slideInVertically { height -> height } + fadeIn())
                    .togetherWith(slideOutVertically { height -> -height } + fadeOut())
            },
            label = "tooltipAnimation",
        ) { index ->
            Text(
                text = tooltipMessages.getOrElse(index) { tooltipMessages.first() },
                style = MoaTheme.typography.b2_500,
                color = MoaTheme.colors.textHighEmphasis,
            )
        }
    }
}

@Composable
private fun TodaySalarySection(
    todaySalary: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.working_today_salary_label),
            style = MoaTheme.typography.b1_500,
            color = MoaTheme.colors.textMediumEmphasis,
        )

        Spacer(Modifier.height(MoaTheme.spacing.spacing4))

        Row(
            verticalAlignment = Alignment.Bottom,
        ) {
            RollingDigitsText(
                text = todaySalary,
            )

            Spacer(Modifier.width(4.dp))

            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = stringResource(R.string.working_currency_won),
                style = MoaTheme.typography.t2_700,
                color = MoaTheme.colors.textHighEmphasis,
            )
        }
    }
}

@Composable
private fun RollingDigitsText(
    text: String,
) {
    Row {
        text.forEach { char ->
            if (char.isDigit()) {
                AnimatedContent(
                    targetState = char,
                    transitionSpec = {
                        (slideInVertically { height -> height } + fadeIn(animationSpec = tween(150)))
                            .togetherWith(slideOutVertically { height -> -height } + fadeOut(animationSpec = tween(150)))
                    },
                    label = "digitAnimation",
                ) { digit ->
                    Text(
                        text = digit.toString(),
                        style = MoaTheme.typography.h1_700,
                        color = MoaTheme.colors.textHighEmphasis,
                    )
                }
            } else {
                Text(
                    text = char.toString(),
                    style = MoaTheme.typography.h1_700,
                    color = MoaTheme.colors.textHighEmphasis,
                )
            }
        }
    }
}

@Composable
private fun CoinGraph(
    modifier: Modifier = Modifier,
    isOnVacation: Boolean = false,
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        val imageWidth = maxWidth * (96f / 375f)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds(),
            contentAlignment = Alignment.TopCenter,
        ) {
            val coinImage = if (isOnVacation) {
                R.drawable.img_blue_coin_progress
            } else {
                R.drawable.img_coin_progress
            }
            Image(
                modifier = Modifier.width(imageWidth),
                painter = painterResource(coinImage),
                contentDescription = stringResource(R.string.working_coin_icon_description),
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.TopCenter,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MoaTheme.colors.bgPrimary,
                        )
                    )


                ),
        )
    }
}

@Composable
private fun WorkingStatusSection(
    workStatus: WorkStatus,
    elapsedTime: String,
    progress: Float,
    startTime: String,
    endTime: String,
    isOnVacation: Boolean,
    onAdjustScheduleClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
        ) {
            Column {
                WorkStatusTag(workStatus = workStatus)

                Spacer(Modifier.height(MoaTheme.spacing.spacing12))

                Text(
                    text = elapsedTime,
                    style = MoaTheme.typography.h1_700,
                    color = MoaTheme.colors.textHighEmphasis,
                )
            }

            Spacer(Modifier.weight(1f))

            MoaTertiaryButton(
                onClick = onAdjustScheduleClick,
            ) {
                Text(
                    text = stringResource(R.string.working_adjust_schedule),
                    style = MoaTheme.typography.b2_500,
                )
            }
        }

        Spacer(Modifier.height(MoaTheme.spacing.spacing24))

        WorkProgressBar(
            progress = progress,
            startTime = startTime,
            endTime = endTime,
            isOnVacation = isOnVacation,
        )
    }
}

@Composable
private fun WorkStatusTag(
    workStatus: WorkStatus,
) {
    val (text, borderColor, textColor) = when (workStatus) {
        WorkStatus.WORKING -> Triple(
            stringResource(R.string.working_status_working),
            Color(0xFF4ADE80),
            Color(0xFF4ADE80),
        )
        WorkStatus.LUNCH_TIME -> Triple(
            stringResource(R.string.working_status_lunch),
            Color(0xFF60A5FA),
            Color(0xFF60A5FA),
        )
        WorkStatus.VACATION -> Triple(
            stringResource(R.string.working_status_vacation),
            Color(0xFF60A5FA),
            Color(0xFF60A5FA),
        )
        WorkStatus.OVERTIME -> Triple(
            stringResource(R.string.working_status_overtime),
            Color(0xFFEF4444),
            Color(0xFFEF4444),
        )
    }

    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(MoaTheme.radius.radius8),
            )
            .padding(
                horizontal = MoaTheme.spacing.spacing8,
                vertical = MoaTheme.spacing.spacing4,
            ),
    ) {
        Text(
            text = text,
            style = MoaTheme.typography.b2_500,
            color = textColor,
        )
    }
}

@Composable
private fun WorkProgressBar(
    progress: Float,
    startTime: String,
    endTime: String,
    isOnVacation: Boolean = false,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 300),
        label = "progressAnimation",
    )

    val progressColors = if (isOnVacation) {
        listOf(Color(0xFF60A5FA), Color(0xFF3B82F6))
    } else {
        listOf(Color(0xFF4ADE80), Color(0xFF22C55E))
    }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MoaTheme.colors.containerSecondary),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        brush = Brush.horizontalGradient(colors = progressColors)
                    ),
            )
        }

        Spacer(Modifier.height(MoaTheme.spacing.spacing4))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = startTime,
                style = MoaTheme.typography.c1_400,
                color = MoaTheme.colors.textLowEmphasis,
            )

            Text(
                text = endTime,
                style = MoaTheme.typography.c1_400,
                color = MoaTheme.colors.textLowEmphasis,
            )
        }
    }
}

@Composable
private fun WorkCompletionSection(
    accumulatedSalary: String,
    workTimeDisplay: String,
    onContinueWorking: () -> Unit,
    onComplete: () -> Unit,
    onWorkTimeClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        WorkCompletionInfoCard(
            accumulatedSalary = accumulatedSalary,
            workTimeDisplay = workTimeDisplay,
            onWorkTimeClick = onWorkTimeClick,
        )

        Spacer(Modifier.height(MoaTheme.spacing.spacing24))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MoaTheme.spacing.spacing12),
        ) {
            MoaVacationButton(
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                onClick = onContinueWorking,
            ) {
                Text(
                    text = stringResource(R.string.working_completion_continue),
                    style = MoaTheme.typography.t3_700,
                )
            }

            MoaPrimaryButton(
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                onClick = onComplete,
            ) {
                Text(
                    text = stringResource(R.string.working_completion_complete),
                    style = MoaTheme.typography.t3_700,
                )
            }
        }
    }
}

@Composable
private fun WorkCompletionInfoCard(
    accumulatedSalary: String,
    workTimeDisplay: String,
    onWorkTimeClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MoaTheme.colors.containerPrimary,
                shape = RoundedCornerShape(MoaTheme.radius.radius16),
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MoaTheme.spacing.spacing20,
                    vertical = MoaTheme.spacing.spacing16,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.working_completion_accumulated_salary_label),
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textMediumEmphasis,
            )

            Spacer(Modifier.width(MoaTheme.spacing.spacing12))

            Text(
                text = "${accumulatedSalary}원",
                style = MoaTheme.typography.t3_700,
                color = MoaTheme.colors.textHighEmphasis,
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = MoaTheme.spacing.spacing20),
            thickness = 1.dp,
            color = MoaTheme.colors.dividerSecondary,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onWorkTimeClick)
                .padding(
                    horizontal = MoaTheme.spacing.spacing20,
                    vertical = MoaTheme.spacing.spacing16,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.working_completion_work_time_label),
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textMediumEmphasis,
            )

            Spacer(Modifier.width(MoaTheme.spacing.spacing12))

            Text(
                text = workTimeDisplay,
                style = MoaTheme.typography.t3_700,
                color = MoaTheme.colors.textHighEmphasis,
            )

            Spacer(Modifier.weight(1f))

            Icon(
                painter = painterResource(R.drawable.ic_24_chevron_right),
                contentDescription = null,
                tint = MoaTheme.colors.textLowEmphasis,
            )
        }
    }
}

@Preview
@Composable
private fun WorkingScreenPreview() {
    MoaTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MoaTheme.colors.bgPrimary),
        ) {
            WorkingScreen(
                uiState = WorkingUiState(),
                onIntent = {},
            )
        }
    }
}

@Preview
@Composable
private fun WorkStatusTagPreview() {
    MoaTheme {
        Row(
            modifier = Modifier
                .background(MoaTheme.colors.bgPrimary)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            WorkStatusTag(WorkStatus.WORKING)
            WorkStatusTag(WorkStatus.LUNCH_TIME)
            WorkStatusTag(WorkStatus.VACATION)
            WorkStatusTag(WorkStatus.OVERTIME)
        }
    }
}