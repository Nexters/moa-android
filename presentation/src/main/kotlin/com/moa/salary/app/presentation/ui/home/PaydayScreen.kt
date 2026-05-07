package com.moa.salary.app.presentation.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.moa.salary.app.presentation.R
import com.moa.salary.app.presentation.designsystem.theme.MoaTheme
import java.time.LocalDate

@Composable
fun PaydayScreen(salary: String) {
    val month = LocalDate.now().monthValue
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.salary))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = true,
        iterations = LottieConstants.IterateForever,
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MoaTheme.colors.dimPrimary),
    ) {
        Image(
            painter = painterResource(R.drawable.blur),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.weight(2f))

            LottieAnimation(
                modifier = Modifier.size(width = 140.dp, height = 100.dp),
                composition = composition,
                progress = { progress },
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing8))

            Text(
                text = "${month}월 받는 월급",
                color = MoaTheme.colors.textMediumEmphasis,
                style = MoaTheme.typography.b2_500,
            )

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = salary,
                    style = MoaTheme.typography.h2_700.copy(fontFeatureSettings = "tnum"),
                    color = MoaTheme.colors.textGreen,
                )

                Spacer(Modifier.width(4.dp))

                Text(
                    modifier = Modifier.padding(bottom = 4.dp),
                    text = stringResource(R.string.working_currency_won),
                    style = MoaTheme.typography.h3_500,
                    color = MoaTheme.colors.textMediumEmphasis,
                )
            }

            Spacer(Modifier.height(MoaTheme.spacing.spacing8))

            Text(
                text = "오늘은 월급날!\n" +
                        "한 달간 열심히 일한 보상이에요",
                color = MoaTheme.colors.textHighEmphasis,
                style = MoaTheme.typography.t3_500,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "월급액은 입력한 월급/연봉을 기준으로 계산돼요.",
                color = MoaTheme.colors.textMediumEmphasis,
                style = MoaTheme.typography.c1_400,
            )

            Spacer(Modifier.weight(3f))
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PaydayScreenPreview() {
    MoaTheme {
        PaydayScreen(salary = "3,000,000")
    }
}
