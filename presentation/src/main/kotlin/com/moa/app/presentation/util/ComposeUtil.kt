package com.moa.app.presentation.util

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalDensity

@Composable
fun rememberIsKeyboardOpen(): State<Boolean> {
    val imeInsets = WindowInsets.ime
    val density = LocalDensity.current
    return rememberUpdatedState(imeInsets.getBottom(density) > 0)
}