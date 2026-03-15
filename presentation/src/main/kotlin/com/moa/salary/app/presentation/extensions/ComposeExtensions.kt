package com.moa.salary.app.presentation.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun TextUnit.toFixedSp(): TextUnit {
    val density = LocalDensity.current
    return with(density) {
        this@toFixedSp.value.dp.toSp()
    }
}

@Composable
fun TextStyle.toFixedSize(): TextStyle {
    return this.copy(
        fontSize = fontSize.toFixedSp(),
    )
}