package com.moa.app.presentation.ui.home.afterwork

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.component.MoaDateLocationBar
import com.moa.app.presentation.designsystem.component.MoaPrimaryButton
import com.moa.app.presentation.designsystem.theme.MoaTheme
import com.moa.app.presentation.navigation.HomeNavigation
import com.moa.app.presentation.ui.home.afterwork.model.AfterWorkIntent
import com.moa.app.presentation.ui.home.afterwork.model.AfterWorkUiState

@Composable
fun AfterWorkScreen(
    args: HomeNavigation.AfterWork,
    viewModel: AfterWorkViewModel = hiltViewModel<AfterWorkViewModel, AfterWorkViewModel.Factory> { factory ->
        factory.create(args)
    },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AfterWorkScreen(
        uiState = uiState,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun AfterWorkScreen(
    uiState: AfterWorkUiState,
    onIntent: (AfterWorkIntent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MoaTheme.spacing.spacing20),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
            Spacer(Modifier.height(MoaTheme.spacing.spacing16))

            MoaDateLocationBar(
                date = uiState.dateDisplay,
                location = uiState.location,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing32))

            Box(
                modifier = Modifier.size(80.dp),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.ic_full_coin),
                    contentDescription = stringResource(R.string.after_work_coin_description),
                )

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
                            onIntent(AfterWorkIntent.DismissConfetti)
                        }
                    }

                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                        modifier = Modifier
                            .wrapContentSize(unbounded = true)
                            .size(200.dp),
                    )
                }
            }

            Spacer(Modifier.height(MoaTheme.spacing.spacing16))

            Text(
                text = stringResource(R.string.after_work_accumulated_salary_title, uiState.month),
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textMediumEmphasis,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing4))

            Row(
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(
                    text = uiState.accumulatedSalary,
                    style = MoaTheme.typography.h1_700,
                    color = MoaTheme.colors.textGreen,
                )

                Spacer(Modifier.width(4.dp))

                Text(
                    modifier = Modifier.padding(bottom = 4.dp),
                    text = stringResource(R.string.after_work_currency_won),
                    style = MoaTheme.typography.t2_700,
                    color = MoaTheme.colors.textHighEmphasis,
                )
            }

            Spacer(Modifier.height(MoaTheme.spacing.spacing32))

            InfoCard(
                todaySalaryWithPlus = uiState.todaySalaryWithPlusDisplay,
                workTimeOrVacation = if (uiState.isOnVacation) {
                    stringResource(R.string.after_work_vacation)
                } else {
                    uiState.workTimeDisplay
                },
            )

            Spacer(Modifier.weight(1f))

            MoaPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                onClick = { onIntent(AfterWorkIntent.ClickCheckWorkHistory) },
            ) {
                Text(
                    text = stringResource(R.string.after_work_check_work_history),
                    style = MoaTheme.typography.t3_700,
                )
            }

            Spacer(Modifier.height(MoaTheme.spacing.spacing24))
    }
}

@Composable
private fun InfoCard(
    todaySalaryWithPlus: String,
    workTimeOrVacation: String,
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
                text = stringResource(R.string.after_work_today_salary_info_label),
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textMediumEmphasis,
            )

            Spacer(Modifier.width(MoaTheme.spacing.spacing12))

            Text(
                text = todaySalaryWithPlus,
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
                .padding(
                    horizontal = MoaTheme.spacing.spacing20,
                    vertical = MoaTheme.spacing.spacing16,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.after_work_work_time_label),
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textMediumEmphasis,
            )

            Spacer(Modifier.width(MoaTheme.spacing.spacing12))

            Text(
                text = workTimeOrVacation,
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
private fun AfterWorkScreenPreview() {
    MoaTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MoaTheme.colors.bgPrimary),
        ) {
            AfterWorkScreen(
                uiState = AfterWorkUiState(showConfetti = false),
                onIntent = {},
            )
        }
    }
}