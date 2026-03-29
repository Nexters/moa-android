package com.moa.salary.app.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moa.salary.app.core.model.work.WorkdayType
import com.moa.salary.app.presentation.R
import com.moa.salary.app.presentation.designsystem.theme.MoaTheme

sealed class ScheduleAdjustOption(val strRes: Int) {
    data object EndWork : ScheduleAdjustOption(R.string.schedule_adjust_option_end_work)

    data object AdjustTime : ScheduleAdjustOption(R.string.schedule_adjust_option_adjust_time)

    data object Vacation : ScheduleAdjustOption(R.string.schedule_adjust_option_vacation)

    data object Work : ScheduleAdjustOption(R.string.schedule_adjust_option_work)

    data object None : ScheduleAdjustOption(R.string.schedule_adjust_option_none)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoaScheduleAdjustBottomSheet(
    type: WorkdayType,
    onDismissRequest: () -> Unit,
    onConfirm: (ScheduleAdjustOption) -> Unit,
) {
    val title = if (type == WorkdayType.WORK) {
        stringResource(R.string.schedule_adjust_work_title)
    } else {
        stringResource(R.string.schedule_adjust_vacation_title)
    }
    val list = remember(type) {
        if (type == WorkdayType.WORK) {
            mutableStateListOf(
                ScheduleAdjustOption.EndWork,
                ScheduleAdjustOption.AdjustTime,
                ScheduleAdjustOption.Vacation,
                ScheduleAdjustOption.None
            )
        } else {
            mutableStateListOf(
                ScheduleAdjustOption.Work,
                ScheduleAdjustOption.None
            )
        }
    }
    val themeColor = if (type == WorkdayType.WORK) {
        MoaTheme.colors.textGreen
    } else {
        MoaTheme.colors.textBlue
    }

    var selectedOption by remember { mutableStateOf<ScheduleAdjustOption?>(null) }

    MoaBottomSheet(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MoaTheme.spacing.spacing20)
                .padding(bottom = MoaTheme.spacing.spacing24),
        ) {
            Text(
                text = title,
                style = MoaTheme.typography.t1_700,
                color = MoaTheme.colors.textHighEmphasis,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing16))

            list.forEachIndexed { index, option ->
                ScheduleOptionItem(
                    text = stringResource(option.strRes),
                    isSelected = selectedOption == option,
                    themeColor = themeColor,
                    onClick = { selectedOption = option },
                )

                if (index != list.lastIndex) {
                    Spacer(Modifier.height(MoaTheme.spacing.spacing8))
                }
            }

            Spacer(Modifier.height(MoaTheme.spacing.spacing20))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MoaTheme.spacing.spacing12),
            ) {
                MoaTertiaryButton(
                    modifier = Modifier.weight(1f),
                    onClick = onDismissRequest,
                ) {
                    Text(
                        text = stringResource(R.string.schedule_adjust_cancel),
                        style = MoaTheme.typography.t3_700,
                    )
                }

                if (type == WorkdayType.WORK) {
                    MoaPrimaryButton(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedOption?.let { onConfirm(it) }
                        },
                        enabled = selectedOption != null,
                    ) {
                        Text(
                            text = stringResource(R.string.schedule_adjust_confirm),
                            style = MoaTheme.typography.t3_700,
                        )
                    }
                } else {
                    MoaBlueButton(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedOption?.let { onConfirm(it) }
                        },
                        enabled = selectedOption != null,
                    ) {
                        Text(
                            text = stringResource(R.string.schedule_adjust_confirm),
                            style = MoaTheme.typography.t3_700,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ScheduleOptionItem(
    text: String,
    isSelected: Boolean,
    themeColor: Color,
    onClick: () -> Unit,
) {
    val borderColor = if (isSelected) {
        themeColor
    } else {
        MoaTheme.colors.dividerSecondary
    }
    val textColor = if (isSelected) {
        themeColor
    } else {
        MoaTheme.colors.textHighEmphasis
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(MoaTheme.radius.radius12))
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(MoaTheme.radius.radius12),
            )
            .background(MoaTheme.colors.containerSecondary)
            .clickable { onClick() }
            .padding(
                horizontal = MoaTheme.spacing.spacing24,
                vertical = MoaTheme.spacing.spacing12,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = text,
            style = MoaTheme.typography.b1_600,
            color = textColor,
        )
    }
}

@Preview
@Composable
private fun MoaScheduleAdjustBottomSheetPreview() {
    MoaTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MoaTheme.colors.bgPrimary)
                .padding(20.dp),
        ) {
            ScheduleOptionItem(
                text = "오늘 휴가예요",
                isSelected = false,
                themeColor = MoaTheme.colors.textGreen,
                onClick = {},
            )
            Spacer(Modifier.height(12.dp))
            ScheduleOptionItem(
                text = "오늘 근무를 마칠 거예요",
                isSelected = true,
                themeColor = MoaTheme.colors.textGreen,
                onClick = {},
            )
        }
    }
}