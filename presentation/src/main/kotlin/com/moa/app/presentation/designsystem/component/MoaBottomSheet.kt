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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
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
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.theme.MoaTheme
import com.moa.app.presentation.ui.onboarding.workschedule.Time

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
    onDismissRequest: () -> Unit,
) {
    MoaBottomSheet(
        visible = visible,
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        MoaTimeBottomSheetContent(
            time = time,
            onDismissRequest = onDismissRequest,
        )
    }
}

@Composable
private fun MoaTimeBottomSheetContent(
    time: Time,
    onDismissRequest: () -> Unit,
) {
    var selectedStartTime by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MoaTheme.spacing.spacing20)
    ) {
        MoaTimeBottomSheetTitleContent(time = time)

        Spacer(Modifier.height(MoaTheme.spacing.spacing16))

        MoaTimeBottomSheetTimeTitleContent(
            time = time,
            selectedStartTime = selectedStartTime,
            onClickStartTime = { selectedStartTime = true },
            onClickEndTime = { selectedStartTime = false },
        )

        Spacer(Modifier.height(MoaTheme.spacing.spacing16))

        MoaTimeBottomSheetTimeContent(

        )

        MoaPrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = MoaTheme.spacing.spacing20)
                .height(64.dp),
            onClick = {
                if (selectedStartTime) {
                    selectedStartTime = false
                } else {
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
}

@Composable
private fun MoaTimeBottomSheetTimeTitleContent(
    time: Time,
    selectedStartTime: Boolean,
    onClickStartTime: () -> Unit,
    onClickEndTime: () -> Unit,
) {
    var startHour by remember {
        mutableStateOf(time.startHour.toString().padStart(2, '0'))
    }
    var startMinute by remember {
        mutableStateOf(time.startMinute.toString().padStart(2, '0'))
    }
    var endHour by remember {
        mutableStateOf(time.endHour.toString().padStart(2, '0'))
    }
    var endMinute by remember {
        mutableStateOf(time.endMinute.toString().padStart(2, '0'))
    }

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
            if (time is Time.Work) {
                Text(
                    text = "출근",
                    style = MoaTheme.typography.b2_500,
                    color = MoaTheme.colors.textLowEmphasis,
                )
            }

            Text(
                text = "$startHour:$startMinute",
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
            if (time is Time.Work) {
                Text(
                    text = "퇴근",
                    style = MoaTheme.typography.b2_500,
                    color = MoaTheme.colors.textLowEmphasis,
                )
            }

            Text(
                text = "$endHour:$endMinute",
                style = if (!selectedStartTime) MoaTheme.typography.t1_700 else MoaTheme.typography.t1_500,
                color = if (!selectedStartTime) MoaTheme.colors.textGreen else MoaTheme.colors.textLowEmphasis,
            )
        }
    }
}

@Composable
private fun MoaTimeBottomSheetTimeContent(

) {
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
    ) {

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
                    onDismissRequest = { }
                )
            }
        }
    }
}