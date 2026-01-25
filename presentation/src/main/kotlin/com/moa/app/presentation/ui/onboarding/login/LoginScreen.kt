package com.moa.app.presentation.ui.onboarding.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
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
            Image(
                modifier = Modifier.widthIn(max = 400.dp),
                painter = painterResource(R.drawable.img_login_logo),
                contentDescription = "Login Image",
            )

            Spacer(Modifier.weight(1f))

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

            Spacer(Modifier.height(MoaTheme.spacing.spacing24))
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