package com.moa.app.presentation.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moa.app.presentation.designsystem.theme.Gray90
import com.moa.app.presentation.designsystem.theme.MoaTheme

@Composable
fun MoaPrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(32.dp),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val containerColor = when {
        !enabled -> MoaTheme.colors.btnPrimaryDisabled
        isPressed -> MoaTheme.colors.btnPrimaryPressed
        else -> MoaTheme.colors.btnPrimaryEnable
    }
    val contentColor = when {
        !enabled -> MoaTheme.colors.textDisabled
        else -> Gray90
    }

    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        border = border,
        contentPadding = contentPadding,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContentColor = contentColor,
            disabledContainerColor = containerColor
        )
    ) {
        content()
    }
}

@Composable
fun MoaSecondaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(32.dp),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val containerColor = when {
        !enabled -> MoaTheme.colors.containerSecondary
        isPressed -> MoaTheme.colors.containerSecondary
        else -> MoaTheme.colors.containerPrimary
    }
    val contentColor = when {
        !enabled -> MoaTheme.colors.textDisabled
        else -> MoaTheme.colors.textHighEmphasis
    }

    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        border = BorderStroke(1.dp, MoaTheme.colors.dividerSecondary),
        contentPadding = contentPadding,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContentColor = contentColor,
            disabledContainerColor = containerColor
        )
    ) {
        content()
    }
}

@Composable
fun MoaVacationButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(32.dp),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val containerColor = when {
        !enabled -> MoaTheme.colors.btnTertiaryDisabled
        isPressed -> MoaTheme.colors.btnTertiaryPressed
        else -> MoaTheme.colors.btnTertiaryEnable
    }
    val contentColor = when {
        !enabled -> MoaTheme.colors.textDisabled
        else -> Gray90
    }

    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        border = border,
        contentPadding = contentPadding,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContentColor = contentColor,
            disabledContainerColor = containerColor
        )
    ) {
        content()
    }
}

@Composable
fun MoaTertiaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(32.dp),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val containerColor = when {
        !enabled -> MoaTheme.colors.btnTertiaryDisabled
        isPressed -> MoaTheme.colors.btnTertiaryPressed
        else -> MoaTheme.colors.btnTertiaryEnable
    }
    val contentColor = when {
        !enabled -> MoaTheme.colors.textDisabled
        else -> Gray90
    }

    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        border = border,
        contentPadding = contentPadding,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContentColor = contentColor,
            disabledContainerColor = containerColor
        )
    ) {
        content()
    }
}

@Preview
@Composable
private fun MoaButtonPreview() {
    MoaTheme {
        MoaPrimaryButton(
            onClick = {}
        ) {
            Text(text = "Test")
        }
    }
}