package com.moa.app.presentation.ui.setting.withdraw

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.component.MoaTopAppBar
import com.moa.app.presentation.designsystem.theme.MoaTheme

@Composable
fun WithDrawScreen() {
    // TODO 탈퇴 화면 구현
}

@Composable
private fun WithDrawScreen(
    onIntent: (WithDrawIntent) -> Unit,
) {
    Scaffold(
        topBar = {
            MoaTopAppBar(
                title = {
                    Text(
                        text = "회원 탈퇴",
                        style = MoaTheme.typography.t3_500,
                        color = MoaTheme.colors.textHighEmphasis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onIntent(WithDrawIntent.ClickBack) }) {
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
                text = "헤어지게 되어 아쉬워요..",
                style = MoaTheme.typography.b2_400,
                color = MoaTheme.colors.textMediumEmphasis,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing4))

            Text(
                text = "탈퇴 사유를 알려주시면\n" +
                        "더 나은 서비스를 제공하기 위해\n" +
                        "노력할게요.",
                style = MoaTheme.typography.t1_700,
                color = MoaTheme.colors.textHighEmphasis,
            )
        }
    }
}

sealed interface WithDrawIntent {
    data object ClickBack : WithDrawIntent
}

@Preview
@Composable
private fun WithDrawScreenPreview() {
    MoaTheme {
        WithDrawScreen()
    }
}