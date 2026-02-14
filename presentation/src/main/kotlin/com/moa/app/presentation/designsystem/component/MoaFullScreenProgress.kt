package com.moa.app.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.theme.MoaTheme

@Composable
fun MoaFullScreenProgress() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.moa_loader)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = true,
        iterations = LottieConstants.IterateForever
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MoaTheme.colors.bgPrimary),
        contentAlignment = Alignment.Center,
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress }
        )
    }
}

@Preview
@Composable
private fun MoaFullScreenProgressPreview() {
    MoaTheme {
        MoaFullScreenProgress()
    }
}