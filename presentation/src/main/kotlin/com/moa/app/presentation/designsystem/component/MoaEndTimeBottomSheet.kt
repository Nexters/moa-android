package com.moa.app.presentation.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.theme.MoaTheme
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoaEndTimeBottomSheet(
    title: String,
    startHour: Int,
    startMinute: Int,
    initialEndHour: Int,
    initialEndMinute: Int,
    leftButtonText: String? = null,
    onLeftButtonClick: (() -> Unit)? = null,
    onDismissRequest: () -> Unit,
    onConfirm: (hour: Int, minute: Int) -> Unit,
) {
    var selectedHour by remember { mutableIntStateOf(initialEndHour) }
    var selectedMinute by remember { mutableIntStateOf(initialEndMinute) }

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
                text = title,
                style = MoaTheme.typography.t1_700,
                color = MoaTheme.colors.textHighEmphasis,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing20))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(R.string.after_work_clock_in),
                        style = MoaTheme.typography.c1_400,
                        color = MoaTheme.colors.textLowEmphasis,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "${startHour.toString().padStart(2, '0')}:${startMinute.toString().padStart(2, '0')}",
                        style = MoaTheme.typography.t2_700,
                        color = MoaTheme.colors.textHighEmphasis,
                    )
                }

                Spacer(Modifier.width(MoaTheme.spacing.spacing24))

                Text(
                    text = "→",
                    style = MoaTheme.typography.t2_700,
                    color = MoaTheme.colors.textLowEmphasis,
                )

                Spacer(Modifier.width(MoaTheme.spacing.spacing24))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(R.string.after_work_clock_out),
                        style = MoaTheme.typography.c1_400,
                        color = MoaTheme.colors.textLowEmphasis,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "${selectedHour.toString().padStart(2, '0')}:${selectedMinute.toString().padStart(2, '0')}",
                        style = MoaTheme.typography.t2_700,
                        color = MoaTheme.colors.textGreen,
                        textDecoration = TextDecoration.Underline,
                    )
                }
            }

            Spacer(Modifier.height(MoaTheme.spacing.spacing24))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MoaWheelPicker(
                    modifier = Modifier.width(80.dp),
                    items = (0..23).toImmutableList(),
                    initialSelectedIndex = selectedHour,
                    onItemSelected = { selectedHour = it },
                    itemToString = { "${it}시" },
                )

                Spacer(Modifier.width(MoaTheme.spacing.spacing16))

                MoaWheelPicker(
                    modifier = Modifier.width(80.dp),
                    items = (0..59).toImmutableList(),
                    initialSelectedIndex = selectedMinute,
                    onItemSelected = { selectedMinute = it },
                    itemToString = { "${it}분" },
                )
            }

            Spacer(Modifier.height(MoaTheme.spacing.spacing24))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MoaTheme.spacing.spacing12),
            ) {
                MoaVacationButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp),
                    onClick = { onLeftButtonClick?.invoke() ?: onDismissRequest() },
                ) {
                    Text(
                        text = leftButtonText ?: stringResource(R.string.after_work_cancel),
                        style = MoaTheme.typography.t3_700,
                    )
                }

                MoaPrimaryButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp),
                    onClick = { onConfirm(selectedHour, selectedMinute) },
                ) {
                    Text(
                        text = stringResource(R.string.after_work_confirm),
                        style = MoaTheme.typography.t3_700,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MoaEndTimeBottomSheetPreview() {
    MoaTheme {
        MoaEndTimeBottomSheet(
            title = "얼마나 더 일하나요?",
            startHour = 9,
            startMinute = 20,
            initialEndHour = 18,
            initialEndMinute = 0,
            onDismissRequest = {},
            onConfirm = { _, _ -> },
        )
    }
}