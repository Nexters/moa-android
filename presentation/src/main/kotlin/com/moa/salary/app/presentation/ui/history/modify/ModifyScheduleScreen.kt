package com.moa.salary.app.presentation.ui.history.modify

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moa.salary.app.core.extensions.makeTimeString
import com.moa.salary.app.core.extensions.toKoreanDateString
import com.moa.salary.app.core.model.onboarding.Time
import com.moa.salary.app.core.model.work.WorkdayType
import com.moa.salary.app.presentation.R
import com.moa.salary.app.presentation.designsystem.component.MoaPrimaryButton
import com.moa.salary.app.presentation.designsystem.component.MoaRow
import com.moa.salary.app.presentation.designsystem.component.MoaTertiaryButton
import com.moa.salary.app.presentation.designsystem.component.MoaTimeBottomSheet
import com.moa.salary.app.presentation.designsystem.component.MoaTopAppBar
import com.moa.salary.app.presentation.designsystem.theme.MoaTheme
import com.moa.salary.app.presentation.model.HistoryNavigation
import java.time.LocalDate

@Composable
fun ModifyScheduleScreen(
    args: HistoryNavigation.ModifySchedule.ModifyScheduleArgs,
    viewModel: ModifyScheduleViewModel = hiltViewModel(
        creationCallback = { factory: ModifyScheduleViewModel.Factory ->
            factory.create(args)
        }
    ),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ModifyScheduleScreen(
        uiState = uiState,
        onIntent = viewModel::onIntent,
    )

    if (uiState.showDateBottomSheet) {

    }

    if (uiState.showTimeBottomSheet) {
        MoaTimeBottomSheet(
            time = uiState.time,
            title = stringResource(R.string.schedule_form_work_time_title),
            onPositive = { viewModel.onIntent(ModifyScheduleIntent.SetTime(it)) },
            onDismissRequest = {
                viewModel.onIntent(
                    ModifyScheduleIntent.SetShowTimeBottomSheet(
                        false
                    )
                )
            }
        )
    }
}

@Composable
private fun ModifyScheduleScreen(
    uiState: ModifyScheduleUiState,
    onIntent: (ModifyScheduleIntent) -> Unit,
) {
    Scaffold(
        topBar = {
            MoaTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onIntent(ModifyScheduleIntent.ClickBack) }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_24_arrow_left),
                            contentDescription = stringResource(R.string.history_back_description),
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
                text = stringResource(
                    if (uiState.isEditMode) {
                        R.string.schedule_form_title_edit
                    } else {
                        R.string.schedule_form_title_add
                    }
                ),
                style = MoaTheme.typography.t1_700,
                color = MoaTheme.colors.textHighEmphasis,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing32))

            Text(
                text = stringResource(R.string.schedule_form_select_date),
                style = MoaTheme.typography.b2_500,
                color = MoaTheme.colors.textMediumEmphasis,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing8))

            MoaRow(
                modifier = Modifier.clickable {
                    onIntent(
                        ModifyScheduleIntent.SetShowDateBottomSheet(
                            true
                        )
                    )
                },
                leadingContent = {
                    Text(
                        text = uiState.date.toKoreanDateString(),
                        style = MoaTheme.typography.t2_700,
                        color = MoaTheme.colors.textHighEmphasis,
                    )
                },
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing24))

            Text(
                text = stringResource(R.string.schedule_form_schedule_type_question),
                style = MoaTheme.typography.b2_500,
                color = MoaTheme.colors.textMediumEmphasis,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing8))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MoaTheme.spacing.spacing12)
            ) {
                WorkdayType.entries.forEach {
                    WorkdayTypeButton(
                        modifier = Modifier.weight(1f),
                        text = it.value,
                        isSelected = uiState.selectedWorkdayType == it,
                        onClick = { onIntent(ModifyScheduleIntent.SetWorkdayType(it)) },
                    )
                }
            }

            if (uiState.selectedWorkdayType == WorkdayType.WORK) {
                Spacer(Modifier.height(MoaTheme.spacing.spacing24))

                Text(
                    text = stringResource(R.string.schedule_form_work_time_label),
                    style = MoaTheme.typography.b2_500,
                    color = MoaTheme.colors.textMediumEmphasis,
                )

                Spacer(Modifier.height(MoaTheme.spacing.spacing8))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 56.dp)
                        .clip(RoundedCornerShape(MoaTheme.radius.radius12))
                        .clickable { onIntent(ModifyScheduleIntent.SetShowTimeBottomSheet(true)) }
                        .background(color = MoaTheme.colors.containerPrimary)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = makeTimeString(uiState.time.startHour, uiState.time.startMinute),
                        style = MoaTheme.typography.t2_700,
                        color = MoaTheme.colors.textHighEmphasis,
                        textAlign = TextAlign.Center,
                    )
                    Icon(
                        modifier = Modifier.padding(horizontal = MoaTheme.spacing.spacing12),
                        painter = painterResource(R.drawable.ic_24_arrow_right),
                        contentDescription = null,
                        tint = MoaTheme.colors.textLowEmphasis,
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        text = makeTimeString(uiState.time.endHour, uiState.time.endMinute),
                        style = MoaTheme.typography.t2_700,
                        color = MoaTheme.colors.textHighEmphasis,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = MoaTheme.spacing.spacing24),
            ) {
                MoaTertiaryButton(
                    modifier = Modifier.weight(1f),
                    onClick = { onIntent(ModifyScheduleIntent.ClickCancel) },
                ) {
                    Text(
                        text = stringResource(R.string.schedule_form_cancel),
                        style = MoaTheme.typography.t3_700,
                    )
                }

                Spacer(Modifier.width(MoaTheme.spacing.spacing12))

                MoaPrimaryButton(
                    modifier = Modifier.weight(1f),
                    onClick = { onIntent(ModifyScheduleIntent.ClickConfirm) },
                ) {
                    Text(
                        text = stringResource(R.string.schedule_form_confirm),
                        style = MoaTheme.typography.t3_700,
                    )
                }
            }
        }
    }
}


@Composable
private fun WorkdayTypeButton(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val backgroundColor = if (isSelected) {
        MoaTheme.colors.containerSecondary
    } else {
        MoaTheme.colors.containerPrimary
    }
    val textColor = if (isSelected) {
        MoaTheme.colors.textHighEmphasis
    } else {
        MoaTheme.colors.textLowEmphasis
    }

    Box(
        modifier = modifier
            .heightIn(min = 56.dp)
            .clip(RoundedCornerShape(MoaTheme.radius.radius12))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(MoaTheme.spacing.spacing16),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MoaTheme.typography.t2_700,
            color = textColor,
        )
    }
}

sealed interface ModifyScheduleIntent {
    data object ClickBack : ModifyScheduleIntent

    @JvmInline
    value class SetShowDateBottomSheet(val show: Boolean) : ModifyScheduleIntent

    @JvmInline
    value class SetDate(val date: LocalDate) : ModifyScheduleIntent

    @JvmInline
    value class SetWorkdayType(val type: WorkdayType) : ModifyScheduleIntent

    @JvmInline
    value class SetShowTimeBottomSheet(val show: Boolean) : ModifyScheduleIntent

    @JvmInline
    value class SetTime(val time: Time) : ModifyScheduleIntent

    data object ClickCancel : ModifyScheduleIntent
    data object ClickConfirm : ModifyScheduleIntent
}

@Preview
@Composable
private fun ModifyScheduleScreenPreview() {
    MoaTheme {
        ModifyScheduleScreen(
            uiState = ModifyScheduleUiState(
                isEditMode = true,
                date = LocalDate.now(),
                selectedWorkdayType = WorkdayType.WORK,
                time = Time(9, 0, 18, 0),
            ),
            onIntent = {},
        )
    }
}