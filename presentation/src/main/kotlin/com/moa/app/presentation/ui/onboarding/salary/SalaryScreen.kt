package com.moa.app.presentation.ui.onboarding.salary

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.component.MoaFilledTextField
import com.moa.app.presentation.designsystem.component.MoaPrimaryButton
import com.moa.app.presentation.designsystem.component.MoaTopAppBar
import com.moa.app.presentation.designsystem.theme.MoaTheme
import com.moa.app.presentation.designsystem.transformation.CurrencyOutputTransformation
import com.moa.app.presentation.designsystem.transformation.SalaryInputTransformation
import com.moa.app.presentation.model.SalaryType
import com.moa.app.presentation.ui.onboarding.OnboardingNavigationArgs

@Composable
fun SalaryScreen(
    args: OnboardingNavigationArgs,
    viewModel: SalaryViewModel = hiltViewModel(
        creationCallback = { factory: SalaryViewModel.Factory ->
            factory.create(args)
        }
    ),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SalaryScreen(
        uiState = uiState,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun SalaryScreen(
    uiState: SalaryUiState,
    onIntent: (SalaryIntent) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        if (uiState.salaryTextField.text.isNotBlank()) {
            focusRequester.requestFocus()
        }
    }

    Scaffold(
        topBar = {
            MoaTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onIntent(SalaryIntent.ClickBack) }) {
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
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = MoaTheme.spacing.spacing20),
        ) {
            Spacer(Modifier.height(MoaTheme.spacing.spacing20))

            Text(
                text = "얼마씩 받고 있나요?",
                color = MoaTheme.colors.textHighEmphasis,
                style = MoaTheme.typography.t1_700,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing8))

            Text(
                text = "세전, 세후 상관없이 보고 싶은 금액을 입력해주세요.",
                color = MoaTheme.colors.textMediumEmphasis,
                style = MoaTheme.typography.b2_400,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing32))

            Text(
                text = "급여 유형",
                color = MoaTheme.colors.textMediumEmphasis,
                style = MoaTheme.typography.b2_400,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing8))

            SalaryTypeSelector(
                selectedType = uiState.selectedSalaryType,
                onIntent = onIntent,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing24))

            Text(
                text = "금액",
                color = MoaTheme.colors.textMediumEmphasis,
                style = MoaTheme.typography.b2_400,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing8))

            MoaFilledTextField(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .fillMaxWidth(),
                state = uiState.salaryTextField,
                placeholder = "0",
                trailingText = "원",
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                inputTransformation = SalaryInputTransformation(),
                outputTransformation = CurrencyOutputTransformation(),
            )

            Spacer(Modifier.weight(1f))

            Spacer(Modifier.height(MoaTheme.spacing.spacing32))

            MoaPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = MoaTheme.spacing.spacing24)
                    .height(64.dp),
                enabled = uiState.salaryTextField.text.isNotBlank(),
                onClick = {
                    focusManager.clearFocus()
                    onIntent(SalaryIntent.ClickNext)
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

@Composable
private fun SalaryTypeSelector(
    selectedType: SalaryType,
    onIntent: (SalaryIntent) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MoaTheme.spacing.spacing12)
    ) {
        SalaryType.entries.forEach {
            SalaryTypeItem(
                text = it.title,
                selected = selectedType == it,
                onClick = { onIntent(SalaryIntent.SelectSalaryType(it)) }
            )
        }
    }
}

@Composable
private fun RowScope.SalaryTypeItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Text(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(MoaTheme.radius.radius12))
            .background(
                color = MoaTheme.colors.containerPrimary,
                shape = RoundedCornerShape(MoaTheme.radius.radius12)
            )
            .padding(vertical = MoaTheme.spacing.spacing12)
            .clickable { onClick() },
        text = text,
        color = if (selected) MoaTheme.colors.textHighEmphasis else MoaTheme.colors.textDisabled,
        style = if (selected) MoaTheme.typography.t2_700 else MoaTheme.typography.t2_500,
        textAlign = TextAlign.Center,
    )
}

sealed interface SalaryIntent {
    data object ClickBack : SalaryIntent

    @JvmInline
    value class SelectSalaryType(val type: SalaryType) : SalaryIntent
    data object ClickNext : SalaryIntent
}

@Preview
@Composable
private fun SalaryScreenPreview() {
    MoaTheme {
        SalaryScreen(
            uiState = SalaryUiState(),
            onIntent = {},
        )
    }
}