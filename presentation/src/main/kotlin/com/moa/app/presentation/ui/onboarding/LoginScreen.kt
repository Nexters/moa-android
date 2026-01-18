package com.moa.app.presentation.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.theme.MoaTheme

@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel()) {
    LoginScreen(
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun LoginScreen(onIntent: (LoginIntent) -> Unit) {
    Scaffold(containerColor = MoaTheme.colors.bgPrimary) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(2f))

            Image(
                painter = painterResource(R.drawable.img_white_logo),
                contentDescription = "Login Image",
            )

            Text(
                text = "실시간으로 월급이 쌓이는 경험!",
                style = MoaTheme.typography.b1_400,
                color = MoaTheme.colors.textMediumEmphasis,
            )

            Spacer(Modifier.weight(2.5f))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = MoaTheme.spacing.spacing16),
                shape = RoundedCornerShape(32.dp),
                onClick = { onIntent(LoginIntent.ClickKakao) },
            ) {
                Text(
                    text = "카카오로 계속하기",
                    style = MoaTheme.typography.t3_700,
                    color = MoaTheme.colors.textHighEmphasis,
                )
            }

            Spacer(Modifier.height(12.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = MoaTheme.spacing.spacing16),
                shape = RoundedCornerShape(32.dp),
                onClick = { onIntent(LoginIntent.ClickApple) },
            ) {
                Text(
                    text = "Apple로 계속하기",
                    style = MoaTheme.typography.t3_700,
                    color = MoaTheme.colors.textHighEmphasis,
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

sealed interface LoginIntent {
    data object ClickKakao : LoginIntent
    data object ClickApple : LoginIntent
}

@Preview
@Composable
private fun LoginScreenPreview() {
    MoaTheme {
        LoginScreen(
            onIntent = {},
        )
    }
}