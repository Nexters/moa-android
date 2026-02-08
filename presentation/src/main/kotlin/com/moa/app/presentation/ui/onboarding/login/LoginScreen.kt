package com.moa.app.presentation.ui.onboarding.login

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.theme.MoaTheme

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

@Preview
@Composable
private fun LoginScreenPreview() {
    MoaTheme {
        LoginScreen(
            onClickKakao = {},
        )
    }
}