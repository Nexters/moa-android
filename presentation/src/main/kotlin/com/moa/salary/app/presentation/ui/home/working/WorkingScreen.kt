package com.moa.salary.app.presentation.ui.home.working

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.moa.salary.app.core.model.onboarding.Time
import com.moa.salary.app.core.model.work.Home
import com.moa.salary.app.core.model.work.WorkdayType
import com.moa.salary.app.presentation.R
import com.moa.salary.app.presentation.designsystem.component.MoaPrimaryButton
import com.moa.salary.app.presentation.designsystem.component.MoaScheduleAdjustBottomSheet
import com.moa.salary.app.presentation.designsystem.component.MoaTertiaryButton
import com.moa.salary.app.presentation.designsystem.component.MoaTimeBottomSheet
import com.moa.salary.app.presentation.designsystem.component.MoaTooltipBanner
import com.moa.salary.app.presentation.designsystem.component.ScheduleAdjustOption
import com.moa.salary.app.presentation.designsystem.theme.MoaTheme
import com.moa.salary.app.presentation.model.HomeNavigation

@Composable
fun WorkingScreen(
    args: HomeNavigation.Working,
    viewModel: WorkingViewModel = hiltViewModel<WorkingViewModel, WorkingViewModel.Factory> { factory ->
        factory.create(args)
    },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onIntent(WorkingIntent.GetHome)
    }

    LaunchedEffect(uiState.confettiProgress) {
        if (uiState.confettiProgress == 1f) {
            viewModel.onIntent(WorkingIntent.ShowConfetti(true))
        }
    }

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
            type = uiState.home.type,
            onDismissRequest = { onIntent(WorkingIntent.ShowScheduleAdjustBottomSheet(false)) },
            onConfirm = { option ->
                when (option) {
                    ScheduleAdjustOption.Vacation -> onIntent(
                        WorkingIntent.SelectChangeType(
                            WorkdayType.VACATION
                        )
                    )

                    ScheduleAdjustOption.EndWork -> onIntent(WorkingIntent.SelectEndWork)
                    ScheduleAdjustOption.AdjustTime -> onIntent(WorkingIntent.SelectAdjustTime)
                    ScheduleAdjustOption.Work -> onIntent(WorkingIntent.SelectChangeType(WorkdayType.WORK))
                    ScheduleAdjustOption.None -> onIntent(WorkingIntent.SelectChangeType(WorkdayType.NONE))
                }
            },
        )
    }

    if (uiState.showTimeBottomSheet) {
        MoaTimeBottomSheet(
            time = Time(
                startHour = uiState.home.startHour,
                startMinute = uiState.home.startMinute,
                endHour = uiState.home.endHour,
                endMinute = uiState.home.endMinute,
            ),
            title = stringResource(R.string.working_time_bottom_sheet_title),
            onPositive = { time ->
                onIntent(
                    WorkingIntent.UpdateWorkTime(
                        startHour = time.startHour,
                        startMinute = time.startMinute,
                        endHour = time.endHour,
                        endMinute = time.endMinute,
                        type = uiState.home.type,
                    )
                )
            },
            onDismissRequest = { onIntent(WorkingIntent.DismissTimeBottomSheet) },
        )
    }

    if (uiState.showMoreWorkBottomSheet) {
        MoaTimeBottomSheet(
            time = Time(
                startHour = uiState.home.startHour,
                startMinute = uiState.home.startMinute,
                endHour = uiState.home.endHour,
                endMinute = uiState.home.endMinute,
            ),
            title = stringResource(R.string.working_more_work_title),
            negativeText = stringResource(R.string.schedule_adjust_cancel),
            endTimeOnly = true,
            onNegative = { onIntent(WorkingIntent.ShowMoreWorkBottomSheet(false)) },
            onPositive = { time ->
                onIntent(WorkingIntent.ConfirmMoreWork(time.endHour, time.endMinute))
            },
            onDismissRequest = { onIntent(WorkingIntent.ShowMoreWorkBottomSheet(false)) },
        )
    }

    if (uiState.showWorkTimeEditBottomSheet) {
        MoaTimeBottomSheet(
            time = Time(
                startHour = uiState.home.startHour,
                startMinute = uiState.home.startMinute,
                endHour = uiState.home.endHour,
                endMinute = uiState.home.endMinute,
            ),
            title = stringResource(R.string.working_work_time_edit_title),
            negativeText = stringResource(R.string.working_today_vacation),
            onNegative = { onIntent(WorkingIntent.ClickTodayVacation) },
            onPositive = { time ->
                onIntent(
                    WorkingIntent.UpdateWorkTime(
                        startHour = time.startHour,
                        startMinute = time.startMinute,
                        endHour = time.endHour,
                        endMinute = time.endMinute,
                        type = uiState.home.type,
                    )
                )
            },
            onDismissRequest = { onIntent(WorkingIntent.ShowWorkTimeEditBottomSheet(false)) },
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
                animationSpec = tween(durationMillis = 500),
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
                        TooltipBanner(
                            monthSalaryDisplay = uiState.monthSalaryDisplay,
                            todaySalary = uiState.todaySalary,
                            remainingHours = uiState.remainingHours,
                            currentIndex = uiState.currentTooltipIndex,
                            type = uiState.home.type,
                        )

                        Spacer(Modifier.height(MoaTheme.spacing.spacing20))
                    }

                    TodaySalarySection(
                        todaySalaryDisplay = uiState.todaySalaryDisplay,
                    )

                    Spacer(Modifier.height(22.dp))

                    CoinGraph(
                        modifier = Modifier.height(coinGraphHeight),
                        type = uiState.home.type,
                    )
                }
            }

            if (uiState.showWorkCompletionOverlay) {
                WorkCompletionSection(
                    accumulatedSalary = uiState.totalSalaryDisplay,
                    workTimeDisplay = "${uiState.startTimeDisplay} - ${uiState.endTimeDisplay}",
                    onContinueWorking = { onIntent(WorkingIntent.ShowMoreWorkBottomSheet(true)) },
                    onComplete = { onIntent(WorkingIntent.ClickCompleteWork) },
                    onWorkTimeClick = { onIntent(WorkingIntent.ShowWorkTimeEditBottomSheet(true)) },
                )
            } else {
                WorkingStatusSection(
                    elapsedTime = uiState.elapsedTimeDisplay,
                    progress = uiState.progress,
                    startTime = uiState.startTimeDisplay,
                    endTime = uiState.endTimeDisplay,
                    type = uiState.home.type,
                    onAdjustScheduleClick = {
                        onIntent(
                            WorkingIntent.ShowScheduleAdjustBottomSheet(
                                true
                            )
                        )
                    },
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
                    onIntent(WorkingIntent.ShowConfetti(false))
                }
            }

            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.fillMaxSize(),
                alignment = Alignment.TopStart,
            )
        }

    }
}

@Composable
private fun TooltipBanner(
    monthSalaryDisplay: String,
    todaySalary: Long,
    remainingHours: Int,
    currentIndex: Int,
    type: WorkdayType,
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

    val tooltipMessages = if (type == WorkdayType.VACATION) {
        listOf(stringResource(R.string.working_tooltip_vacation))
    } else {
        listOf(
            stringResource(R.string.working_tooltip_month_salary, monthSalaryDisplay),
            purchasableMessage,
            stringResource(R.string.working_tooltip_remaining_time, remainingHours),
        )
    }

    val displayIndex = if (type == WorkdayType.VACATION) 0 else currentIndex % tooltipMessages.size
    val targetMessage = tooltipMessages.getOrElse(displayIndex) { tooltipMessages.first() }
    var visibleIndex by remember { mutableIntStateOf(displayIndex) }
    var visibleMessage by remember { mutableStateOf(targetMessage) }
    val bannerAlpha = remember { Animatable(1f) }

    LaunchedEffect(displayIndex, targetMessage) {
        if (visibleIndex == displayIndex) {
            visibleMessage = targetMessage
            bannerAlpha.snapTo(1f)
            return@LaunchedEffect
        }

        bannerAlpha.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 300),
        )
        visibleIndex = displayIndex
        visibleMessage = targetMessage
        bannerAlpha.snapTo(0f)
        bannerAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 300),
        )
    }

    MoaTooltipBanner(
        modifier = Modifier.alpha(bannerAlpha.value),
        text = visibleMessage,
    )
}

@Composable
private fun TodaySalarySection(
    todaySalaryDisplay: String,
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
                text = todaySalaryDisplay,
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
                            .togetherWith(slideOutVertically { height -> -height } + fadeOut(
                                animationSpec = tween(150)
                            ))
                    },
                    label = "digitAnimation",
                ) { digit ->
                    Text(
                        text = digit.toString(),
                        style = MoaTheme.typography.h1_700.copy(fontFeatureSettings = "tnum"),
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
    type: WorkdayType,
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        val imageWidth = maxWidth * (112f / 375f)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds(),
            contentAlignment = Alignment.TopCenter,
        ) {
            val coinImage = if (type == WorkdayType.VACATION) {
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
    elapsedTime: String,
    progress: Float,
    startTime: String,
    endTime: String,
    type: WorkdayType,
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
                WorkdayTypeTag(type = type)

                Spacer(Modifier.height(MoaTheme.spacing.spacing12))

                Text(
                    text = elapsedTime,
                    style = MoaTheme.typography.h1_700,
                    color = MoaTheme.colors.textHighEmphasis,
                )
            }

            Spacer(Modifier.weight(1f))

            MoaTertiaryButton(
                height = 36.dp,
                onClick = onAdjustScheduleClick,
            ) {
                Text(
                    text = stringResource(R.string.working_adjust_schedule),
                    style = MoaTheme.typography.b1_600,
                )
            }
        }

        Spacer(Modifier.height(MoaTheme.spacing.spacing24))

        WorkProgressBar(
            progress = progress,
            startTime = startTime,
            endTime = endTime,
            type = type,
        )
    }
}

@Composable
private fun WorkdayTypeTag(
    type: WorkdayType,
) {
    val (text, color) = when (type) {
        WorkdayType.WORK -> Pair(
            stringResource(R.string.working_status_working),
            MoaTheme.colors.textGreen
        )

        else -> Pair(
            stringResource(R.string.working_status_vacation),
            MoaTheme.colors.textBlue,
        )
    }

    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = color,
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
            color = color,
        )
    }
}

@Composable
private fun WorkProgressBar(
    progress: Float,
    startTime: String,
    endTime: String,
    type: WorkdayType,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 300),
        label = "progressAnimation",
    )

    val progressColor = if (type == WorkdayType.VACATION) {
        MoaTheme.colors.textBlue
    } else {
        MoaTheme.colors.textGreen
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
                    .background(progressColor),
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
            MoaTertiaryButton(
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

sealed interface WorkingIntent {
    data object GetHome : WorkingIntent

    @JvmInline
    value class ShowScheduleAdjustBottomSheet(val show: Boolean) : WorkingIntent

    @JvmInline
    value class ShowWorkTimeEditBottomSheet(val show: Boolean) : WorkingIntent

    @JvmInline
    value class ShowMoreWorkBottomSheet(val show: Boolean) : WorkingIntent

    @JvmInline
    value class ShowConfetti(val show: Boolean) : WorkingIntent

    data object DismissTimeBottomSheet : WorkingIntent

    @JvmInline
    value class SelectChangeType(val type: WorkdayType) : WorkingIntent
    data object SelectEndWork : WorkingIntent
    data object SelectAdjustTime : WorkingIntent

    data class UpdateWorkTime(
        val startHour: Int,
        val startMinute: Int,
        val endHour: Int,
        val endMinute: Int,
        val type: WorkdayType,
    ) : WorkingIntent

    data object ClickCompleteWork : WorkingIntent

    data object ClickTodayVacation : WorkingIntent
    data class ConfirmMoreWork(val endHour: Int, val endMinute: Int) : WorkingIntent
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
                uiState = WorkingUiState(
                    home = Home(
                        workplace = "모아주식회사",
                        workedEarnings = 1000000,
                        standardSalary = 1000000,
                        dailyPay = 100000,
                        type = WorkdayType.WORK,
                        startHour = 9,
                        startMinute = 0,
                        endHour = 18,
                        endMinute = 0,
                    ),
                    showWorkCompletionOverlay = false,
                ),
                onIntent = {},
            )
        }
    }
}
