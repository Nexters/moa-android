package com.moa.app.presentation.ui.onboarding.workplace

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun WorkPlaceScreen(viewModel: WorkPlaceViewModel = hiltViewModel()) {
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

    Scaffold(
        topBar = {
            MoaTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onIntent(WorkPlaceIntent.ClickBack) }) {
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
        ) {
            Spacer(Modifier.weight(1f))

            MoaTextFieldWithDescription(
                modifier = Modifier.padding(horizontal = MoaTheme.spacing.spacing20),
                description1 = "근무지",
                state = workPlaceTextFieldState,
                description2 = "에서 일해요",
                placeholder = "근무지를 입력해주세요",
            )

            Spacer(Modifier.weight(1f))

            MoaPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = MoaTheme.spacing.spacing20,
                        end = MoaTheme.spacing.spacing20,
                        bottom = MoaTheme.spacing.spacing20,
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