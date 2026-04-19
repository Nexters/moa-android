package com.moa.salary.app.presentation.ui.onboarding.salary

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moa.salary.app.core.extensions.makePriceString
import com.moa.salary.app.core.model.onboarding.Payroll
import com.moa.salary.app.core.util.Constants
import com.moa.salary.app.presentation.R
import com.moa.salary.app.presentation.designsystem.component.CurrencyInputTransformation
import com.moa.salary.app.presentation.designsystem.component.CurrencyOutputTransformation
import com.moa.salary.app.presentation.designsystem.component.MoaFilledTextField
import com.moa.salary.app.presentation.designsystem.component.MoaPrimaryButton
import com.moa.salary.app.presentation.designsystem.component.MoaTopAppBar
import com.moa.salary.app.presentation.designsystem.theme.MoaTheme
import com.moa.salary.app.presentation.model.OnboardingNavigation
import com.moa.salary.app.presentation.model.PosthogEvent
import com.moa.salary.app.presentation.ui.onboarding.nickname.NicknameEvent
import kotlinx.coroutines.delay

@Composable
fun SalaryScreen(
    args: OnboardingNavigation.Salary.SalaryNavigationArgs,
    viewModel: SalaryViewModel = hiltViewModel(
        creationCallback = { factory: SalaryViewModel.Factory ->
            factory.create(args)
        }
    ),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SalaryScreen(
        isOnboarding = args.isOnboarding,
        uiState = uiState,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun SalaryScreen(
    isOnboarding: Boolean,
    uiState: SalaryUiState,
    onIntent: (SalaryIntent) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            MoaTopAppBar(
                title = {
                    if (!isOnboarding) {
                        Text(
                            text = "급여",
                            color = MoaTheme.colors.textHighEmphasis,
                            style = MoaTheme.typography.t3_500,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onIntent(SalaryIntent.ClickBack) }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_24_arrow_left),
                            contentDescription = "Back",
                            tint = MoaTheme.colors.textHighEmphasis,
                        )
                    }
                }
            )
        },
        bottomBar = {
            MoaPrimaryButton(
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding()
                    .fillMaxWidth()
                    .padding(
                        start = MoaTheme.spacing.spacing20,
                        end = MoaTheme.spacing.spacing20,
                        bottom = MoaTheme.spacing.spacing24,
                    ),
                enabled = uiState.salaryNumber >= Constants.MAN,
                onClick = {
                    focusManager.clearFocus()
                    onIntent(SalaryIntent.ClickNext)
                },
            ) {
                Text(
                    text = if (isOnboarding) "다음" else "완료",
                    style = MoaTheme.typography.t3_700,
                )
            }
        },
        containerColor = MoaTheme.colors.bgPrimary,
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .verticalScroll(scrollState)
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
                text = "입력한 정보를 바탕으로 실시간 급여가 계산되며,\n급여 정보는 누구에게도 공개되지 않아요.",
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

            SalaryTextField(
                salaryTextField = uiState.salaryTextField,
                salaryNumber = uiState.salaryNumber,
            )
        }
    }
}

@Composable
private fun SalaryTypeSelector(
    selectedType: Payroll.SalaryType,
    onIntent: (SalaryIntent) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MoaTheme.spacing.spacing12)
    ) {
        Payroll.SalaryType.entries.forEach {
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
            .padding(vertical = MoaTheme.spacing.spacing16)
            .clickable { onClick() },
        text = text,
        color = if (selected) MoaTheme.colors.textHighEmphasis else MoaTheme.colors.textDisabled,
        style = if (selected) MoaTheme.typography.t2_700 else MoaTheme.typography.t2_500,
        textAlign = TextAlign.Center,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SalaryTextField(
    salaryTextField: TextFieldState,
    salaryNumber: Int,
) {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val isWarningVisible = salaryNumber < Constants.MAN
    val isImeVisible = WindowInsets.isImeVisible
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(isImeVisible, isFocused) {
        if (isImeVisible && isFocused) {
            delay(200)
            bringIntoViewRequester.bringIntoView()
        }
    }

    Text(
        text = "금액",
        color = MoaTheme.colors.textMediumEmphasis,
        style = MoaTheme.typography.b2_400,
    )

    Spacer(Modifier.height(MoaTheme.spacing.spacing8))

    MoaFilledTextField(
        modifier = Modifier.fillMaxWidth(),
        textFieldModifier = Modifier
            .onFocusChanged { focusState ->
                isFocused = focusState.hasFocus
            },
        state = salaryTextField,
        placeholder = "0",
        trailingText = "원",
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
        ),
        onKeyboardAction = { focusManager.clearFocus() },
        inputTransformation = CurrencyInputTransformation(),
        outputTransformation = CurrencyOutputTransformation(),
    )

    Spacer(Modifier.height(MoaTheme.spacing.spacing8))

    Column(modifier = Modifier.bringIntoViewRequester(bringIntoViewRequester)) {
        if (isWarningVisible) {
            Row {
                Image(
                    painter = painterResource(R.drawable.ic_16_warning),
                    contentDescription = null,
                )

                Spacer(Modifier.width(MoaTheme.spacing.spacing4))

                Text(
                    text = "금액은 1만원 이상 입력해 주세요",
                    color = MoaTheme.colors.textError,
                    style = MoaTheme.typography.b2_500,
                )
            }
        } else {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                text = salaryTextField.text.toString().makePriceString(),
                color = MoaTheme.colors.textGreen,
                style = MoaTheme.typography.b2_500,
            )
        }

        Spacer(Modifier.height(MoaTheme.spacing.spacing24))
    }
}

sealed interface SalaryIntent {
    data object ClickBack : SalaryIntent

    @JvmInline
    value class SelectSalaryType(val type: Payroll.SalaryType) : SalaryIntent
    data object ClickNext : SalaryIntent
}

sealed class SalaryEvent(
    override val event: String,
    override val properties: Map<String, Any>? = null,
) : PosthogEvent {
    data class ClickNext(val isModified: Boolean) : SalaryEvent(
        event = "salary_next_clicked",
        properties = mapOf("is_modified" to isModified)
    )
}

@Preview
@Composable
private fun SalaryScreenPreview() {
    MoaTheme {
        SalaryScreen(
            isOnboarding = false,
            uiState = SalaryUiState(),
            onIntent = {},
        )
    }
}
