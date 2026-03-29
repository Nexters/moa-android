package com.moa.salary.app.presentation.ui.onboarding.nickname

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.text.input.then
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.moa.salary.app.core.extensions.isKoreanEnglishOrDigit
import com.moa.salary.app.presentation.R
import com.moa.salary.app.presentation.designsystem.component.MoaPrimaryButton
import com.moa.salary.app.presentation.designsystem.component.MoaTextFieldWithDescription
import com.moa.salary.app.presentation.designsystem.component.MoaTopAppBar
import com.moa.salary.app.presentation.designsystem.theme.MoaTheme
import com.moa.salary.app.presentation.model.OnboardingNavigation
import com.moa.salary.app.presentation.util.rememberIsKeyboardOpen

@Composable
fun NicknameScreen(
    args: OnboardingNavigation.Nickname.NicknameNavigationArgs,
    viewModel: NicknameViewModel = hiltViewModel(
        creationCallback = { factory: NicknameViewModel.Factory ->
            factory.create(args)
        }
    )
) {
    BackHandler {
        viewModel.onIntent(NicknameIntent.ClickBack)
    }

    NicknameScreen(
        isOnboarding = args.isOnboarding,
        nickNameTextFieldState = viewModel.nicknameTextFieldState,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun NicknameScreen(
    isOnboarding: Boolean,
    nickNameTextFieldState: TextFieldState,
    onIntent: (NicknameIntent) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val isKeyboardOpen by rememberIsKeyboardOpen()

    Scaffold(
        topBar = {
            MoaTopAppBar(
                title = {
                    if (!isOnboarding) {
                        Text(
                            text = "닉네임 수정",
                            color = MoaTheme.colors.textHighEmphasis,
                            style = MoaTheme.typography.t3_500,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onIntent(NicknameIntent.ClickBack) }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_24_arrow_left),
                            contentDescription = "Back",
                            tint = MoaTheme.colors.textHighEmphasis,
                        )
                    }
                },
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
                description2 = if (isOnboarding) "로 가입할래요" else "로 수정할게요",
                placeholder = "닉네임을 입력해주세요",
                inputTransformation = InputTransformation
                    .maxLength(10)
                    .then {
                        val filtered = asCharSequence().filter { it.isKoreanEnglishOrDigit() }

                        replace(0, length, filtered)
                    },
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
                    .clickable { onIntent(NicknameIntent.ClickRandom) },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_16_restart),
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

            if (isKeyboardOpen) {
                Text(
                    text = "10자까지 입력할 수 있어요",
                    style = MoaTheme.typography.b2_500,
                    color = MoaTheme.colors.textLowEmphasis,
                )

                Spacer(Modifier.height(MoaTheme.spacing.spacing12))
            }

            MoaPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = MoaTheme.spacing.spacing20,
                        end = MoaTheme.spacing.spacing20,
                        bottom = MoaTheme.spacing.spacing24,
                    )
                    .height(64.dp),
                enabled = nickNameTextFieldState.text.isNotBlank(),
                onClick = {
                    focusManager.clearFocus()
                    onIntent(NicknameIntent.ClickNext)
                },
            ) {
                Text(
                    text = if (isOnboarding) "다음" else "완료",
                    style = MoaTheme.typography.t3_700,
                )
            }
        }
    }
}

sealed interface NicknameIntent {
    data object ClickBack : NicknameIntent
    data object ClickRandom : NicknameIntent
    data object ClickNext : NicknameIntent
}

@Preview
@Composable
private fun NicknameScreenPreview() {
    MoaTheme {
        NicknameScreen(
            isOnboarding = false,
            nickNameTextFieldState = rememberTextFieldState(),
            onIntent = {},
        )
    }
}