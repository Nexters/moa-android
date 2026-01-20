package com.moa.app.presentation.ui.onboarding.nickname

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.component.MoaPrimaryButton
import com.moa.app.presentation.designsystem.component.MoaTextFieldWithDescription
import com.moa.app.presentation.designsystem.component.MoaTopAppBar
import com.moa.app.presentation.designsystem.theme.MoaTheme

@Composable
fun NickNameScreen(viewModel: NickNameViewModel = hiltViewModel()) {
    NickNameScreen(
        nickNameTextFieldState = viewModel.nickNameTextFieldState,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun NickNameScreen(
    nickNameTextFieldState: TextFieldState,
    onIntent: (NickNameIntent) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            MoaTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onIntent(NickNameIntent.ClickBack) }) {
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
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.weight(1f))

            MoaTextFieldWithDescription(
                modifier = Modifier.padding(horizontal = MoaTheme.spacing.spacing20),
                description1 = "닉네임",
                state = nickNameTextFieldState,
                description2 = "로 가입할래요",
                placeholder = "닉네임을 입력해주세요",
                inputTransformation = InputTransformation.maxLength(10),
            )

            Spacer(Modifier.height(32.dp))

            Row(
                modifier = Modifier
                    .background(
                        color = MoaTheme.colors.containerPrimary,
                        shape = RoundedCornerShape(MoaTheme.radius.radius8),
                    )
                    .padding(
                        vertical = MoaTheme.spacing.spacing8,
                        horizontal = MoaTheme.spacing.spacing12,
                    )
                    .clickable { onIntent(NickNameIntent.ClickRandom) },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_refresh),
                    contentDescription = "Refresh",
                    tint = MoaTheme.colors.textHighEmphasis,
                )

                Spacer(Modifier.width(4.dp))

                Text(
                    text = "랜덤변경",
                    style = MoaTheme.typography.b2_500,
                    color = MoaTheme.colors.textHighEmphasis,
                )
            }

            Spacer(Modifier.weight(2f))

            MoaPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = MoaTheme.spacing.spacing20,
                        end = MoaTheme.spacing.spacing20,
                        bottom = MoaTheme.spacing.spacing20,
                    )
                    .height(64.dp),
                enabled = nickNameTextFieldState.text.isNotBlank(),
                onClick = {
                    focusManager.clearFocus()
                    onIntent(NickNameIntent.ClickNext)
                },
            ) {
                Text(
                    text = "다음",
                    style = MoaTheme.typography.t3_700,
                )
            }
        }
    }
}

sealed interface NickNameIntent {
    data object ClickBack : NickNameIntent
    data object ClickRandom : NickNameIntent
    data object ClickNext : NickNameIntent
}

@Preview
@Composable
private fun NickNameScreenPreview() {
    MoaTheme {
        NickNameScreen(
            nickNameTextFieldState = rememberTextFieldState(),
            onIntent = {},
        )
    }
}