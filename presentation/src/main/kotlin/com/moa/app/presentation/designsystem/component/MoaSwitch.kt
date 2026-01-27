package com.moa.app.presentation.designsystem.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.moa.app.presentation.designsystem.theme.Gray40
import com.moa.app.presentation.designsystem.theme.Gray60
import com.moa.app.presentation.designsystem.theme.MoaTheme

@Composable
fun MoaSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    trackWidth: Dp = 44.dp,
    trackHeight: Dp = 24.dp,
    thumbRadius: Dp = 10.dp,
    thumbPadding: Dp = 2.dp,
    checkedTrackColor: Color = MoaTheme.colors.textGreen,
    uncheckedTrackColor: Color = Gray60,
    checkedThumbColor: Color = Color.White,
    uncheckedThumbColor: Color = Color.White,
    disabledTrackColor: Color = Gray60,
    disabledThumbColor: Color = Gray40
) {
    val thumbPosition by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = tween(durationMillis = 200),
        label = "thumbPosition"
    )

    val trackColor = when {
        !enabled -> disabledTrackColor
        checked -> checkedTrackColor
        else -> uncheckedTrackColor
    }

    val thumbColor = when {
        !enabled -> disabledThumbColor
        checked -> checkedThumbColor
        else -> uncheckedThumbColor
    }

    Canvas(
        modifier = modifier
            .size(width = trackWidth, height = trackHeight)
            .clickable(
                enabled = enabled,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onCheckedChange(!checked)
            }
    ) {
        val trackWidthPx = size.width
        val trackHeightPx = size.height
        val cornerRadiusPx = trackHeightPx / 2
        val thumbRadiusPx = thumbRadius.toPx()
        val thumbPaddingPx = thumbPadding.toPx()

        drawRoundRect(
            color = trackColor,
            cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
        )

        val thumbStartX = thumbPaddingPx + thumbRadiusPx
        val thumbEndX = trackWidthPx - thumbPaddingPx - thumbRadiusPx
        val thumbX = thumbStartX + (thumbEndX - thumbStartX) * thumbPosition
        val thumbY = trackHeightPx / 2

        drawCircle(
            color = thumbColor,
            radius = thumbRadiusPx,
            center = Offset(thumbX, thumbY)
        )
    }
}

@Preview
@Composable
private fun MoaSwitchPreview() {
    MoaTheme {
        var checked by remember { mutableStateOf(false) }

        Box(Modifier.size(300.dp), contentAlignment = Alignment.Center) {
            MoaSwitch(
                checked = checked,
                onCheckedChange = { checked = it },
                modifier = Modifier,
                enabled = false,
            )
        }
    }
}