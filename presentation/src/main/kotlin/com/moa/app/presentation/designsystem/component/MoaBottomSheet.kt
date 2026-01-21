package com.moa.app.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moa.app.core.makeTimeString
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.theme.MoaTheme
import com.moa.app.presentation.model.Time
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoaBottomSheet(
    visible: Boolean,
    sheetState: SheetState = rememberModalBottomSheetState(),
    shape: Shape = RoundedCornerShape(
        topStart = 24.dp,
        topEnd = 24.dp,
    ),
    containerColor: Color = MoaTheme.colors.containerPrimary,
    onDismissRequest: () -> Unit,
    properties: ModalBottomSheetProperties = ModalBottomSheetProperties(),
    content: @Composable ColumnScope.() -> Unit,
) {
    LaunchedEffect(visible) {
        if (visible) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        shape = shape,
        sheetState = sheetState,
        containerColor = containerColor,
        properties = properties,
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoaTimeBottomSheet(
    time: Time,
    visible: Boolean,
    onClickButton: (startHour: Int, startMinute: Int, endHour: Int, endMinute: Int) -> Unit,
    onDismissRequest: () -> Unit,
) {
    MoaBottomSheet(
        visible = visible,
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        MoaTimeBottomSheetContent(
            time = time,
            onClickButton = onClickButton,
            onDismissRequest = onDismissRequest,
        )
    }
}

@Composable
private fun MoaTimeBottomSheetContent(
    time: Time,
    onClickButton: (startHour: Int, startMinute: Int, endHour: Int, endMinute: Int) -> Unit,
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
        MoaTimeBottomSheetTitleContent(time = time)

        Spacer(Modifier.height(MoaTheme.spacing.spacing16))

        MoaTimeBottomSheetTimeContent(
            time = time,
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

        MoaPrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = MoaTheme.spacing.spacing20)
                .height(64.dp),
            enabled = (startHour <= endHour) && (startMinute < endMinute),
            onClick = {
                if (selectedStartTime) {
                    selectedStartTime = false
                } else {
                    onClickButton(startHour, startMinute, endHour, endMinute)
                    onDismissRequest()
                }
            },
        ) {
            Text(
                text = "다음",
                style = MoaTheme.typography.t3_700,
            )
        }
    }
}

@Composable
private fun MoaTimeBottomSheetTitleContent(
    time: Time,
) {
    Text(
        text = when (time) {
            is Time.Work -> "${time.title}을 알려주세요"
            is Time.Lunch -> "${time.title}을 알려주세요"
        },
        style = MoaTheme.typography.t1_700,
        color = MoaTheme.colors.textHighEmphasis,
    )

    if (time.description.isNotEmpty()) {
        Spacer(Modifier.height(MoaTheme.spacing.spacing4))

        Text(
            text = time.description,
            style = MoaTheme.typography.b1_400,
            color = MoaTheme.colors.textMediumEmphasis,
        )
    }
}

@Composable
private fun MoaTimeBottomSheetTimeContent(
    time: Time,
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
                text = when (time) {
                    is Time.Work -> "출근"
                    is Time.Lunch -> "시작"
                },
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
            painter = painterResource(R.drawable.icon_arrow_right),
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
                text = when (time) {
                    is Time.Work -> "퇴근"
                    is Time.Lunch -> "종료"
                },
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun MoaBottomSheetPreview() {
    MoaTheme {
        Column(Modifier.fillMaxSize()) {
            MoaBottomSheet(
                visible = true,
                onDismissRequest = {}
            ) {
                MoaTimeBottomSheetContent(
                    time = Time.Work(
                        startHour = 9,
                        startMinute = 0,
                        endHour = 18,
                        endMinute = 0,
                    ),
                    onClickButton = { _, _, _, _ -> },
                    onDismissRequest = { }
                )
            }
        }
    }
}