package com.moa.salary.app.presentation.designsystem.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.moa.salary.app.core.extensions.toZeroString
import com.moa.salary.app.presentation.designsystem.theme.MoaTheme
import kotlinx.coroutines.delay

@Composable
fun MoaRollingText(
    text: String,
    textColor: Color,
    animateOnAppear: Boolean = false,
) {
    var displayText by remember { mutableStateOf(if (animateOnAppear) text.toZeroString() else text) }

    LaunchedEffect(text) {
        if (animateOnAppear && displayText.isEmpty()) {
            delay(100)
        }
        displayText = text
    }

    Row {
        displayText.forEach { char ->
            if (char.isDigit()) {
                AnimatedContent(
                    targetState = char,
                    transitionSpec = {
                        (slideInVertically { height -> height } + fadeIn(animationSpec = tween(150)))
                            .togetherWith(slideOutVertically { height -> -height } + fadeOut(
                                animationSpec = tween(150)
                            ))
                    },
                    label = "digitAnimation",
                ) { digit ->
                    Text(
                        text = digit.toString(),
                        style = MoaTheme.typography.h1_700.copy(fontFeatureSettings = "tnum"),
                        color = textColor,
                    )
                }
            } else {
                Text(
                    text = char.toString(),
                    style = MoaTheme.typography.h1_700,
                    color = textColor,
                )
            }
        }
    }
}