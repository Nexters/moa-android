package com.moa.salary.app.presentation.ui.onboarding.login

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.moa.salary.app.core.model.setting.OAuthType
import com.moa.salary.app.presentation.R
import com.moa.salary.app.presentation.designsystem.component.MoaPageIndicator
import com.moa.salary.app.presentation.designsystem.theme.MoaTheme
import com.moa.salary.app.presentation.model.PosthogEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel()) {
    val activity = LocalActivity.current

    LoginScreen(
        onClickKakao = {
            if (activity != null) {
                viewModel.clickKakao(activity)
            }
        }
    )
}

@Composable
private fun LoginScreen(
    onClickKakao: () -> Unit
) {
    val maxSize = Int.MAX_VALUE
    val pagerState = rememberPagerState(initialPage = maxSize / 2) { maxSize }
    var isAutoPagingEnabled by remember { mutableStateOf(true) }
    var isProgrammaticScroll by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = isAutoPagingEnabled) {
        while (isAutoPagingEnabled) {
            delay(2200)
            isProgrammaticScroll = true
            pagerState.animateScrollToPage(
                page = pagerState.currentPage + 1,
                animationSpec = tween(
                    durationMillis = 720,
                    easing = EaseIn
                )
            )
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.isScrollInProgress }
            .collectLatest { isScrolling ->
                if (isScrolling) {
                    if (!isProgrammaticScroll) {
                        isAutoPagingEnabled = false
                    }
                } else {
                    isProgrammaticScroll = false

                    if (!isAutoPagingEnabled) {
                        delay(2000)
                        isAutoPagingEnabled = true
                    }
                }
            }
    }

    Scaffold(containerColor = MoaTheme.colors.bgPrimary) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoginScreenContent(
                pagerState = pagerState,
            )

            MoaPageIndicator(
                pageCount = 3,
                currentPage = pagerState.currentPage % 3,
            )

            Spacer(Modifier.height(32.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = MoaTheme.spacing.spacing16),
                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE500)),
                onClick = onClickKakao,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_24_kakao),
                        contentDescription = "Kakao Login",
                        tint = MoaTheme.colors.textHighEmphasisReverse,
                    )

                    Spacer(Modifier.width(12.dp))

                    Text(
                        text = "카카오로 계속하기",
                        style = MoaTheme.typography.t3_700,
                        color = MoaTheme.colors.textHighEmphasisReverse,
                    )
                }
            }

            Spacer(Modifier.height(MoaTheme.spacing.spacing24))
        }
    }
}

@Composable
private fun ColumnScope.LoginScreenContent(pagerState: PagerState) {
    HorizontalPager(
        modifier = Modifier.weight(1f),
        state = pagerState
    ) { pageCount ->
        when (pageCount % 3) {
            0 -> LoginFirstContent()
            1 -> LoginSecondContent()
            else -> LoginThirdContent()
        }
    }
}

@Composable
private fun LoginFirstContent() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.onboarding1))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = true,
        iterations = LottieConstants.IterateForever,
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        LottieAnimation(
            modifier = Modifier
                .padding(top = 24.dp)
                .widthIn(max = 375.dp)
                .height(333.dp),
            composition = composition,
            progress = { progress },
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(238.dp))

            Image(
                painter = painterResource(R.drawable.img_big_white_logo),
                contentDescription = null,
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "실시간으로 월급이 쌓이는 경험!",
                style = MoaTheme.typography.b1_400,
                color = MoaTheme.colors.textHighEmphasis,
            )
        }
    }
}

@Composable
private fun LoginSecondContent() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.onboarding2))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = true,
        iterations = LottieConstants.IterateForever,
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(26.dp))
                .background(
                    color = MoaTheme.colors.containerPrimary,
                )
                .sizeIn(
                    maxWidth = 375.dp,
                    maxHeight = 420.dp,
                )
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(
                    top = 40.dp,
                    bottom = 32.dp,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "오늘 쌓은 월급",
                style = MoaTheme.typography.b2_500,
                color = MoaTheme.colors.textMediumEmphasis,
            )

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "12,000",
                    style = MoaTheme.typography.h2_700,
                    color = MoaTheme.colors.textHighEmphasis,
                )

                Spacer(Modifier.width(4.dp))

                Text(
                    modifier = Modifier.padding(bottom = 4.dp),
                    text = stringResource(R.string.working_currency_won),
                    style = MoaTheme.typography.h3_500,
                    color = MoaTheme.colors.textMediumEmphasis,
                )
            }

            LottieAnimation(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .weight(1f),
                composition = composition,
                progress = { progress },
            )

            Row {
                Text(
                    text = "오늘도 쌓이는 ",
                    style = MoaTheme.typography.t1_700,
                    color = MoaTheme.colors.textGreen,
                )

                Text(
                    text = "내월급",
                    style = MoaTheme.typography.t1_700,
                    color = MoaTheme.colors.textHighEmphasis,
                )
            }

            Spacer(Modifier.height(6.dp))

            Text(
                text = "출퇴근 시간과 급여만 입력하면\n오늘 번 월급을 자동으로 계산해드려요.",
                style = MoaTheme.typography.b2_400,
                textAlign = TextAlign.Center,
                color = MoaTheme.colors.textMediumEmphasis,
            )
        }
    }
}

@Composable
private fun LoginThirdContent() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.onboarding3))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = true,
        iterations = LottieConstants.IterateForever,
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(26.dp))
                .background(
                    color = MoaTheme.colors.containerPrimary,
                )
                .sizeIn(
                    maxWidth = 375.dp,
                    maxHeight = 420.dp,
                )
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(
                    top = 40.dp,
                    bottom = 32.dp,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "오늘은 월급날!",
                style = MoaTheme.typography.b2_500,
                color = MoaTheme.colors.textMediumEmphasis,
            )

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "3,400,000",
                    style = MoaTheme.typography.h2_700,
                    color = MoaTheme.colors.textHighEmphasis,
                )

                Spacer(Modifier.width(4.dp))

                Text(
                    modifier = Modifier.padding(bottom = 4.dp),
                    text = stringResource(R.string.working_currency_won),
                    style = MoaTheme.typography.h3_500,
                    color = MoaTheme.colors.textMediumEmphasis,
                )
            }

            LottieAnimation(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .weight(1f),
                composition = composition,
                progress = { progress },
            )

            Row {
                Text(
                    text = "당신의 ",
                    style = MoaTheme.typography.t1_700,
                    color = MoaTheme.colors.textHighEmphasis,
                )

                Text(
                    text = "존버",
                    style = MoaTheme.typography.t1_700,
                    color = MoaTheme.colors.textGreen,
                )

                Text(
                    text = "를 함께해요",
                    style = MoaTheme.typography.t1_700,
                    color = MoaTheme.colors.textHighEmphasis,
                )
            }

            Spacer(Modifier.height(6.dp))

            Text(
                text = "월급날만 기다리지 말고\n지금 버는 돈을 보면서 같이 존버해요!.",
                style = MoaTheme.typography.b2_400,
                textAlign = TextAlign.Center,
                color = MoaTheme.colors.textMediumEmphasis,
            )
        }
    }
}

sealed class LoginEvent(
    override val event: String,
    override val properties: Map<String, Any>? = null,
) : PosthogEvent {
    data class ClickLogin(val oauthType: OAuthType) : LoginEvent(
        event = "login_button_clicked",
        properties = mapOf("oauth_type" to oauthType.name)
    )
}

@Preview
@Composable
private fun LoginScreenPreview() {
    MoaTheme {
        LoginScreen(
            onClickKakao = {},
        )
    }
}