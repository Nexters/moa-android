package com.moa.app.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moa.app.core.extensions.makeTimeString
import com.moa.app.core.model.onboarding.Time
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.theme.MoaTheme
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoaTimeBottomSheet(
    time: Time,
    title: String,
    negativeText: String? = null,
    positiveText: String = "확인",
    onNegative: () -> Unit = {},
    onPositive: (Time) -> Unit,
    onDismissRequest: () -> Unit,
) {
    MoaBottomSheet(
        onDismissRequest = onDismissRequest,
    ) {
        MoaTimeBottomSheetContent(
            time = time,
            title = title,
            negativeText = negativeText,
            positiveText = positiveText,
            onNegative = onNegative,
            onPositive = onPositive,
            onDismissRequest = onDismissRequest,
        )
    }
}

@Composable
private fun MoaTimeBottomSheetContent(
    time: Time,
    title: String,
    negativeText: String?,
    positiveText: String,
    onNegative: () -> Unit,
    onPositive: (Time) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var selectedStartTime by remember { mutableStateOf(true) }
    var startHour by remember { mutableIntStateOf(time.startHour) }
    var startMinute by remember { mutableIntStateOf(time.startMinute) }
    var endHour by remember { mutableIntStateOf(time.endHour) }
    var endMinute by remember { mutableIntStateOf(time.endMinute) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MoaTheme.spacing.spacing20)
    ) {
        Text(
            text = title,
            style = MoaTheme.typography.t1_700,
            color = MoaTheme.colors.textHighEmphasis,
        )

        Spacer(Modifier.height(MoaTheme.spacing.spacing16))

        MoaTimeBottomSheetTimeContent(
            selectedStartTime = selectedStartTime,
            startHour = startHour,
            startMinute = startMinute,
            endHour = endHour,
            endMinute = endMinute,
            onSelectedHour = { selectedHour ->
                if (selectedStartTime) {
                    startHour = selectedHour
                } else {
                    endHour = selectedHour
                }
            },
            onSelectedMinute = { selectedMinute ->
                if (selectedStartTime) {
                    startMinute = selectedMinute
                } else {
                    endMinute = selectedMinute
                }
            },
            onClickStartTime = { selectedStartTime = true },
            onClickEndTime = { selectedStartTime = false },
        )

        MoaTimeBottomSheetButtonContent(
            enabled = if (selectedStartTime) {
                true
            } else {
                (endHour > startHour) || (endHour == startHour && endMinute > startMinute)
            },
            negativeText = negativeText,
            positiveText = positiveText,
            onNegative = {
                onNegative()
                onDismissRequest()
            },
            onPositive = {
                if (selectedStartTime) {
                    selectedStartTime = false
                } else {
                    onPositive(Time(startHour, startMinute, endHour, endMinute))
                    onDismissRequest()
                }
            },
        )
    }
}

@Composable
private fun MoaTimeBottomSheetTimeContent(
    selectedStartTime: Boolean,
    startHour: Int,
    startMinute: Int,
    endHour: Int,
    endMinute: Int,
    onSelectedHour: (Int) -> Unit,
    onSelectedMinute: (Int) -> Unit,
    onClickStartTime: () -> Unit,
    onClickEndTime: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MoaTheme.spacing.spacing24),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .clickable { onClickStartTime() },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "출근",
                style = MoaTheme.typography.b2_500,
                color = MoaTheme.colors.textLowEmphasis,
            )

            Text(
                text = makeTimeString(startHour, startMinute),
                style = if (selectedStartTime) MoaTheme.typography.t1_700 else MoaTheme.typography.t1_500,
                color = if (selectedStartTime) MoaTheme.colors.textGreen else MoaTheme.colors.textLowEmphasis,
            )
        }

        Icon(
            modifier = Modifier.padding(horizontal = MoaTheme.spacing.spacing24),
            painter = painterResource(R.drawable.ic_24_arrow_right),
            contentDescription = "ArrowRight",
            tint = MoaTheme.colors.textLowEmphasis,
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .clickable { onClickEndTime() },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "퇴근",
                style = MoaTheme.typography.b2_500,
                color = MoaTheme.colors.textLowEmphasis,
            )

            Text(
                text = makeTimeString(endHour, endMinute),
                style = if (!selectedStartTime) MoaTheme.typography.t1_700 else MoaTheme.typography.t1_500,
                color = if (!selectedStartTime) MoaTheme.colors.textGreen else MoaTheme.colors.textLowEmphasis,
            )
        }
    }

    Spacer(Modifier.height(MoaTheme.spacing.spacing16))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(236.dp)
            .clip(RoundedCornerShape(MoaTheme.radius.radius16))
            .background(
                color = MoaTheme.colors.containerSecondary,
                shape = RoundedCornerShape(MoaTheme.radius.radius16),
            )
            .padding(MoaTheme.spacing.spacing8),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(
                    color = MoaTheme.colors.containerPrimary,
                    shape = RoundedCornerShape(MoaTheme.radius.radius12)
                )
        )

        Row {
            key(selectedStartTime) {
                MoaWheelPicker(
                    modifier = Modifier.width(120.dp),
                    items = (0..23).toList().toImmutableList(),
                    initialSelectedIndex = if (selectedStartTime) {
                        startHour
                    } else {
                        endHour
                    },
                    onItemSelected = onSelectedHour,
                )

                MoaWheelPicker(
                    modifier = Modifier.width(120.dp),
                    items = (0..59).toList().toImmutableList(),
                    initialSelectedIndex = if (selectedStartTime) {
                        startMinute
                    } else {
                        endMinute
                    },
                    onItemSelected = onSelectedMinute,
                )
            }
        }
    }
}

@Composable
private fun MoaTimeBottomSheetButtonContent(
    enabled: Boolean,
    negativeText: String?,
    positiveText: String,
    onNegative: () -> Unit,
    onPositive: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = MoaTheme.spacing.spacing20,
                bottom = MoaTheme.spacing.spacing24,
            )
    ) {
        if (negativeText != null) {
            MoaTertiaryButton(
                modifier = Modifier
                    .weight(1f)
                    .height(64.dp),
                onClick = onNegative,
            ) {
                Text(
                    text = negativeText,
                    style = MoaTheme.typography.t3_700,
                )
            }

            Spacer(modifier = Modifier.padding(MoaTheme.spacing.spacing12))
        }

        MoaPrimaryButton(
            modifier = Modifier
                .weight(1f)
                .height(64.dp),
            enabled = enabled,
            onClick = onPositive,
        ) {
            Text(
                text = positiveText,
                style = MoaTheme.typography.t3_700,
            )
        }
    }
}

@Preview
@Composable
private fun MoaTimeBottomSheetPreview() {
    MoaTheme {
        Column(Modifier.fillMaxSize()) {
            MoaTimeBottomSheet(
                title = "실제 근무 시간을 알려주세요",
                time = Time(
                    startHour = 9,
                    startMinute = 0,
                    endHour = 18,
                    endMinute = 0,
                ),
                negativeText = "오늘 휴가",
                onNegative = {},
                onPositive = { },
                onDismissRequest = {}
            )
        }
    }
}