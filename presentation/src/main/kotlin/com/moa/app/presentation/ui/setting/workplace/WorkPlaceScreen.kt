package com.moa.app.presentation.ui.setting.workplace

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import com.moa.app.presentation.util.rememberIsKeyboardOpen

@Composable
fun WorkPlaceScreen(
    workPlace: String,
    viewModel: WorkPlaceViewModel = hiltViewModel(
        creationCallback = { factory: WorkPlaceViewModel.Factory ->
            factory.create(workPlace)
        }
    ),
) {
    WorkPlaceScreen(
        workPlaceTextFieldState = viewModel.workPlaceTextFieldState,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun WorkPlaceScreen(
    workPlaceTextFieldState: TextFieldState,
    onIntent: (WorkPlaceIntent) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val isKeyboardOpen by rememberIsKeyboardOpen()

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            MoaTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onIntent(WorkPlaceIntent.ClickBack) }) {
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
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .padding(horizontal = MoaTheme.spacing.spacing20),
                description1 = "근무지",
                state = workPlaceTextFieldState,
                description2 = "에서 일해요",
                placeholder = "근무지를 입력해주세요",
                inputTransformation = InputTransformation.maxLength(20)
            )

            Spacer(Modifier.weight(2f))

            if (isKeyboardOpen) {
                Text(
                    text = "20자까지 입력할 수 있어요",
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
                enabled = workPlaceTextFieldState.text.isNotBlank(),
                onClick = {
                    focusManager.clearFocus()
                    onIntent(WorkPlaceIntent.ClickNext)
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

sealed interface WorkPlaceIntent {
    data object ClickBack : WorkPlaceIntent
    data object ClickNext : WorkPlaceIntent
}

@Preview
@Composable
private fun WorkPlaceScreenPreview() {
    MoaTheme {
        WorkPlaceScreen(
            workPlaceTextFieldState = TextFieldState(),
            onIntent = {},
        )
    }
}