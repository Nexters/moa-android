package com.moa.app.presentation.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class MoaRadius(
    val radius4: Dp,
    val radius8: Dp,
    val radius12: Dp,
    val radius16: Dp,
    val radius20: Dp,
    val radius999: Dp,
)

val Radius4 : Dp = 4.dp
val Radius8 : Dp = 8.dp
val Radius12 : Dp = 12.dp
val Radius16 : Dp = 16.dp
val Radius20 : Dp = 20.dp
val Radius999 : Dp = 999.dp