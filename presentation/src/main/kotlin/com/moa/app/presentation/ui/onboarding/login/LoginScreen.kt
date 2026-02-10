package com.moa.app.presentation.ui.onboarding.login

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.component.MoaPageIndicator
import com.moa.app.presentation.designsystem.theme.MoaTheme
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
            delay(3000)
            isProgrammaticScroll = true
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
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
                        delay(5000)
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
                        text = "카카오 로그인",
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
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter,
        ) {
            Image(
                modifier = Modifier.widthIn(max = 400.dp),
                painter = painterResource(
                    when {
                        pageCount % 3 == 0 -> R.drawable.img_first_onboarding
                        pageCount % 3 == 1 -> R.drawable.img_second_onboarding
                        else -> R.drawable.img_third_onboarding
                    }
                ),
                contentDescription = null,
            )
        }
    }
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