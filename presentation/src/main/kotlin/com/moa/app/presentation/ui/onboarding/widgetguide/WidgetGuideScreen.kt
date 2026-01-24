package com.moa.app.presentation.ui.onboarding.widgetguide

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.component.MoaPrimaryButton
import com.moa.app.presentation.designsystem.component.MoaTopAppBar
import com.moa.app.presentation.designsystem.theme.MoaTheme

@Composable
fun WidgetGuideScreen(viewModel: WidgetGuideViewModel = hiltViewModel()) {
    WidgetGuideScreen(
        onIntent = viewModel::onIntent
    )
}

@Composable
private fun WidgetGuideScreen(
    onIntent: (WidgetGuideIntent) -> Unit,
) {
    Scaffold(
        topBar = {
            MoaTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onIntent(WidgetGuideIntent.ClickBack) }) {
                        Icon(
                            painter = painterResource(R.drawable.icon_back),
                            contentDescription = "Back",
                            tint = MoaTheme.colors.textHighEmphasis,
                        )
                    }
                }
            )
        },
        containerColor = MoaTheme.colors.bgPrimary,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = MoaTheme.spacing.spacing20),
        ) {
            Spacer(Modifier.height(MoaTheme.spacing.spacing20))

            Text(
                text = "홈 화면에 위젯을 추가할까요?",
                color = MoaTheme.colors.textHighEmphasis,
                style = MoaTheme.typography.t1_700,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing8))

            Text(
                text = "앱을 열지 않아도 홈 화면에서 오늘 번 돈을\n" +
                        "실시간으로 확인할 수 있어요.",
                color = MoaTheme.colors.textMediumEmphasis,
                style = MoaTheme.typography.b2_400,
            )

            Spacer(Modifier.weight(1f))

            MoaPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                onClick = {
                    // TODO 위젯 추가 로직
                },
            ) {
                Text(
                    text = "위젯 추가하기",
                    style = MoaTheme.typography.t3_700,
                )
            }

            Spacer(Modifier.height(10.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onIntent(WidgetGuideIntent.ClickNext) }
                    .padding(bottom = MoaTheme.spacing.spacing24),
                text = "다음에 할게요",
                style = MoaTheme.typography.t3_700.copy(textDecoration = TextDecoration.Underline),
                color = MoaTheme.colors.textMediumEmphasis,
                textAlign = TextAlign.Center,
            )
        }
    }
}

sealed interface WidgetGuideIntent {
    data object ClickBack : WidgetGuideIntent
    data object ClickNext : WidgetGuideIntent
}

@Preview
@Composable
private fun WidgetGuideScreenPreview() {
    MoaTheme {
        WidgetGuideScreen(
            onIntent = {},
        )
    }
}