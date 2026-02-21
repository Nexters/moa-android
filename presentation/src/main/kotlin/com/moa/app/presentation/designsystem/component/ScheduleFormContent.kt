package com.moa.app.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moa.app.core.model.history.LocalDateModel
import com.moa.app.core.model.onboarding.Time
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.theme.MoaTheme
import com.moa.app.presentation.ui.history.scheduleform.ScheduleInputType

@Composable
fun ScheduleFormContent(
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

            MoaRow(
                modifier = Modifier.clickable(onClick = onTimeClick),
                leadingContent = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = String.format("%02d:%02d", time.startHour, time.startMinute),
                            style = MoaTheme.typography.t2_700,
                            color = MoaTheme.colors.textHighEmphasis,
                        )
                        Icon(
                            modifier = Modifier.padding(horizontal = MoaTheme.spacing.spacing12),
                            painter = painterResource(R.drawable.ic_24_arrow_right),
                            contentDescription = null,
                            tint = MoaTheme.colors.textLowEmphasis,
                        )
                        Text(
                            text = String.format("%02d:%02d", time.endHour, time.endMinute),
                            style = MoaTheme.typography.t2_700,
                            color = MoaTheme.colors.textHighEmphasis,
                        )
                    }
                },
            )
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
fun ScheduleTypeButton(
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

    Row(
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(MoaTheme.radius.radius12))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = MoaTheme.spacing.spacing16),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = text,
            style = MoaTheme.typography.t2_700,
            color = textColor,
        )
    }
}