package com.moa.salary.app.presentation.ui.history.scheduleform

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.moa.salary.app.core.model.work.LocalDateModel
import com.moa.salary.app.core.model.work.Schedule
import com.moa.salary.app.core.model.onboarding.Time
import com.moa.salary.app.presentation.R
import com.moa.salary.app.presentation.designsystem.component.DatePickerBottomSheet
import com.moa.salary.app.presentation.designsystem.component.MoaPrimaryButton
import com.moa.salary.app.presentation.designsystem.component.MoaRow
import com.moa.salary.app.presentation.designsystem.component.MoaTertiaryButton
import com.moa.salary.app.presentation.designsystem.component.MoaTimeBottomSheet
import com.moa.salary.app.presentation.designsystem.component.MoaTopAppBar
import com.moa.salary.app.presentation.designsystem.theme.MoaTheme

@Composable
fun ScheduleFormScreen(
    initialDate: LocalDateModel,
    schedule: Schedule?,
    viewModel: ScheduleFormViewModel = hiltViewModel(
        creationCallback = { factory: ScheduleFormViewModel.Factory ->
            factory.create(initialDate, schedule)
        }
    ),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ScheduleFormScreen(
        uiState = uiState,
        onIntent = viewModel::onIntent,
    )

    if (uiState.showDateBottomSheet) {
        DatePickerBottomSheet(
            selectedDate = uiState.date,
            onConfirm = { viewModel.onIntent(ScheduleFormIntent.SetDate(it)) },
            onDismissRequest = { viewModel.onIntent(ScheduleFormIntent.ShowDateBottomSheet(false)) },
        )
    }

    if (uiState.showTimeBottomSheet) {
        MoaTimeBottomSheet(
            time = uiState.time,
            title = stringResource(R.string.schedule_form_work_time_title),
            onPositive = { viewModel.onIntent(ScheduleFormIntent.SetTime(it)) },
            onDismissRequest = { viewModel.onIntent(ScheduleFormIntent.ShowTimeBottomSheet(false)) },
        )
    }
}

@Composable
private fun ScheduleFormScreen(
    uiState: ScheduleFormUiState,
    onIntent: (ScheduleFormIntent) -> Unit,
) {
    Scaffold(
        topBar = {
            MoaTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onIntent(ScheduleFormIntent.ClickBack) }) {
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
        ScheduleFormContent(
            modifier = Modifier.padding(innerPadding),
            title = stringResource(if (uiState.isEditMode) R.string.schedule_form_title_edit else R.string.schedule_form_title_add),
            date = uiState.date,
            isDateSelected = uiState.isDateSelected,
            scheduleType = uiState.scheduleType,
            time = uiState.time,
            onDateClick = { onIntent(ScheduleFormIntent.ShowDateBottomSheet(true)) },
            onScheduleTypeSelect = { onIntent(ScheduleFormIntent.SelectScheduleType(it)) },
            onTimeClick = { onIntent(ScheduleFormIntent.ShowTimeBottomSheet(true)) },
            onCancelClick = { onIntent(ScheduleFormIntent.ClickCancel) },
            onConfirmClick = { onIntent(ScheduleFormIntent.ClickConfirm) },
        )
    }
}

@Composable
private fun ScheduleFormContent(
    modifier: Modifier = Modifier,
    title: String,
    date: LocalDateModel,
    isDateSelected: Boolean,
    scheduleType: ScheduleInputType,
    time: Time,
    onDateClick: () -> Unit,
    onScheduleTypeSelect: (ScheduleInputType) -> Unit,
    onTimeClick: () -> Unit,
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = MoaTheme.spacing.spacing20),
    ) {
        Spacer(Modifier.height(MoaTheme.spacing.spacing20))

        Text(
            text = title,
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
            modifier = Modifier.clickable(onClick = onDateClick),
            leadingContent = {
                Text(
                    text = date.toDisplayString(),
                    style = if (isDateSelected) MoaTheme.typography.t2_700 else MoaTheme.typography.t2_400,
                    color = if (isDateSelected) MoaTheme.colors.textHighEmphasis else MoaTheme.colors.textLowEmphasis,
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

        Row(modifier = Modifier.fillMaxWidth()) {
            ScheduleTypeButton(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.schedule_form_type_vacation),
                isSelected = scheduleType == ScheduleInputType.VACATION,
                onClick = { onScheduleTypeSelect(ScheduleInputType.VACATION) },
            )

            Spacer(Modifier.width(MoaTheme.spacing.spacing12))

            ScheduleTypeButton(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.schedule_form_type_work),
                isSelected = scheduleType == ScheduleInputType.WORK,
                onClick = { onScheduleTypeSelect(ScheduleInputType.WORK) },
            )
        }

        if (scheduleType == ScheduleInputType.WORK) {
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
                    .clickable(onClick = onTimeClick)
                    .background(color = MoaTheme.colors.containerPrimary)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = makeTimeString(time.startHour, time.startMinute),
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
                    text = makeTimeString(time.endHour, time.endMinute),
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
                modifier = Modifier
                    .weight(1f)
                    .height(64.dp),
                onClick = onCancelClick,
            ) {
                Text(
                    text = stringResource(R.string.schedule_form_cancel),
                    style = MoaTheme.typography.t3_700,
                )
            }

            Spacer(Modifier.width(MoaTheme.spacing.spacing12))

            MoaPrimaryButton(
                modifier = Modifier
                    .weight(1f)
                    .height(64.dp),
                onClick = onConfirmClick,
                enabled = isDateSelected,
            ) {
                Text(
                    text = stringResource(R.string.schedule_form_confirm),
                    style = MoaTheme.typography.t3_700,
                )
            }
        }
    }
}

@Composable
private fun ScheduleTypeButton(
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
            .height(56.dp)
            .clip(RoundedCornerShape(MoaTheme.radius.radius12))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = MoaTheme.spacing.spacing16),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MoaTheme.typography.t2_700,
            color = textColor,
        )
    }
}

@Preview
@Composable
private fun ScheduleFormScreenAddPreview() {
    MoaTheme {
        ScheduleFormScreen(
            uiState = ScheduleFormUiState(
                isEditMode = false,
                date = LocalDateModel(2026, 1, 20),
                isDateSelected = false,
                scheduleType = ScheduleInputType.WORK,
                time = Time(9, 0, 18, 0),
            ),
            onIntent = {},
        )
    }
}

@Preview
@Composable
private fun ScheduleFormScreenEditPreview() {
    MoaTheme {
        ScheduleFormScreen(
            uiState = ScheduleFormUiState(
                isEditMode = true,
                date = LocalDateModel(2026, 1, 20),
                isDateSelected = true,
                scheduleType = ScheduleInputType.VACATION,
                time = Time(9, 0, 18, 0),
            ),
            onIntent = {},
        )
    }
}