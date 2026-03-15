package com.moa.salary.app.presentation.ui.onboarding.widgetguide

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.moa.salary.app.presentation.R
import com.moa.salary.app.presentation.designsystem.component.MoaPrimaryButton
import com.moa.salary.app.presentation.designsystem.component.MoaTopAppBar
import com.moa.salary.app.presentation.designsystem.theme.MoaTheme
import com.moa.salary.app.presentation.ui.widget.MoaWidgetReceiver

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
    val context = LocalContext.current
    val appWidgetManager = remember { AppWidgetManager.getInstance(context) }
    val moaWidgetReceiver = remember { ComponentName(context, MoaWidgetReceiver::class.java) }

    Scaffold(
        topBar = {
            MoaTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onIntent(WidgetGuideIntent.ClickBack) }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_24_arrow_left),
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
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(MoaTheme.spacing.spacing20))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MoaTheme.spacing.spacing20),
                text = "홈 화면에 위젯을 추가할까요?",
                color = MoaTheme.colors.textHighEmphasis,
                style = MoaTheme.typography.t1_700,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing8))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MoaTheme.spacing.spacing20),
                text = "앱을 열지 않아도 홈 화면에서 오늘 번 돈을\n" +
                        "실시간으로 확인할 수 있어요.",
                color = MoaTheme.colors.textMediumEmphasis,
                style = MoaTheme.typography.b2_400,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing20))

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 400.dp),
                painter = painterResource(R.drawable.img_widget_guide),
                contentDescription = "Widget Guide Image",
            )

            Spacer(Modifier.weight(1f))

            MoaPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MoaTheme.spacing.spacing20),
                onClick = {
                    onIntent(WidgetGuideIntent.ClickNext)

                    if (appWidgetManager.isRequestPinAppWidgetSupported) {
                        appWidgetManager.requestPinAppWidget(
                            moaWidgetReceiver,
                            null,
                            null
                        )
                    }
                },
            ) {
                Text(
                    text = "위젯 추가하기",
                    style = MoaTheme.typography.t3_700,
                )
            }

            Spacer(Modifier.height(MoaTheme.spacing.spacing16))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onIntent(WidgetGuideIntent.ClickNext) }
                    .padding(bottom = MoaTheme.spacing.spacing24),
                text = "다음에 할게요",
                style = MoaTheme.typography.b1_600.copy(textDecoration = TextDecoration.Underline),
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