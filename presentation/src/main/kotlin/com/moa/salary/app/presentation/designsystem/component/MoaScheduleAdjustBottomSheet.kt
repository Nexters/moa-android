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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moa.salary.app.presentation.R
import com.moa.salary.app.presentation.designsystem.theme.MoaTheme

enum class ScheduleAdjustOption(val strRes: Int) {
    VACATION(R.string.schedule_adjust_option_vacation),
    END_WORK(R.string.schedule_adjust_option_end_work),
    ADJUST_TIME(R.string.schedule_adjust_option_adjust_time),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoaScheduleAdjustBottomSheet(
    onDismissRequest: () -> Unit,
    onConfirm: (ScheduleAdjustOption) -> Unit,
) {
    var selectedOption by remember { mutableStateOf<ScheduleAdjustOption?>(null) }

    MoaBottomSheet(
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MoaTheme.spacing.spacing20)
                .padding(bottom = MoaTheme.spacing.spacing24),
        ) {
            Text(
                text = stringResource(R.string.schedule_adjust_title),
                style = MoaTheme.typography.t1_700,
                color = MoaTheme.colors.textHighEmphasis,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing16))

            ScheduleAdjustOption.entries.forEachIndexed { index, option ->
                ScheduleOptionItem(
                    text = stringResource(option.strRes),
                    isSelected = selectedOption == option,
                    onClick = { selectedOption = option },
                )

                if (index != ScheduleAdjustOption.entries.lastIndex) {
                    Spacer(Modifier.height(MoaTheme.spacing.spacing8))
                }
            }

            Spacer(Modifier.height(MoaTheme.spacing.spacing20))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MoaTheme.spacing.spacing12),
            ) {
                MoaTertiaryButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp),
                    onClick = onDismissRequest,
                ) {
                    Text(
                        text = stringResource(R.string.schedule_adjust_cancel),
                        style = MoaTheme.typography.t3_700,
                    )
                }

                MoaPrimaryButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp),
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

@Composable
private fun ScheduleOptionItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val borderColor = if (isSelected) {
        MoaTheme.colors.btnPrimaryEnable
    } else {
        MoaTheme.colors.dividerSecondary
    }

    val backgroundColor = MoaTheme.colors.containerSecondary

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
            .background(backgroundColor)
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
            color = MoaTheme.colors.textHighEmphasis,
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
                onClick = {},
            )
            Spacer(Modifier.height(12.dp))
            ScheduleOptionItem(
                text = "오늘 근무를 마칠 거예요",
                isSelected = true,
                onClick = {},
            )
        }
    }
}