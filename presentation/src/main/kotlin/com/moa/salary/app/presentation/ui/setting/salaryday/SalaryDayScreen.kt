package com.moa.salary.app.presentation.ui.setting.salaryday

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moa.salary.app.presentation.R
import com.moa.salary.app.presentation.designsystem.component.MoaPrimaryButton
import com.moa.salary.app.presentation.designsystem.component.MoaRow
import com.moa.salary.app.presentation.designsystem.component.MoaTopAppBar
import com.moa.salary.app.presentation.designsystem.theme.MoaTheme

@Composable
fun SalaryDayScreen(
    day: Int,
    viewModel: SalaryDayViewModel = hiltViewModel(
        creationCallback = { factory: SalaryDayViewModel.Factory ->
            factory.create(day)
        }
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SalaryDayScreen(
        salaryDay = uiState.salaryDay,
        onIntent = viewModel::onIntent,
    )

    if (uiState.showSalaryDayBottomSheet) {
        SalaryDayBottomSheet(
            salaryDay = uiState.salaryDay,
            onConfirm = { viewModel.onIntent(SalaryDateIntent.SetSalaryDay(it)) },
            onDismissRequest = { viewModel.onIntent(SalaryDateIntent.ShowSalaryDayBottomSheet(false)) }
        )
    }
}

@Composable
private fun SalaryDayScreen(
    salaryDay: Int,
    onIntent: (SalaryDateIntent) -> Unit,
) {
    Scaffold(
        topBar = {
            MoaTopAppBar(
                title = {
                    Text(
                        text = "월급일",
                        style = MoaTheme.typography.t3_500,
                        color = MoaTheme.colors.textHighEmphasis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onIntent(SalaryDateIntent.ClickBack) }) {
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = MoaTheme.spacing.spacing20),
        ) {
            Spacer(Modifier.height(MoaTheme.spacing.spacing20))

            Text(
                text = "언제 월급 받나요?",
                style = MoaTheme.typography.t1_700,
                color = MoaTheme.colors.textHighEmphasis,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing32))

            Text(
                text = "월급일",
                style = MoaTheme.typography.b2_500,
                color = MoaTheme.colors.textMediumEmphasis,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing8))

            MoaRow(
                modifier = Modifier.clickable {
                    onIntent(
                        SalaryDateIntent.ShowSalaryDayBottomSheet(
                            true
                        )
                    )
                },
                leadingContent = {
                    Text(
                        text = "${salaryDay}일",
                        style = MoaTheme.typography.t2_700,
                        color = MoaTheme.colors.textHighEmphasis,
                    )
                },
                trailingContent = {
                    Image(
                        modifier = Modifier.rotate(90f),
                        painter = painterResource(R.drawable.ic_24_chevron_right),
                        contentDescription = "Chevron Right",
                        colorFilter = ColorFilter.tint(MoaTheme.colors.textLowEmphasis)
                    )
                }
            )

            Spacer(Modifier.weight(1f))

            MoaPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = MoaTheme.spacing.spacing24),
                onClick = { onIntent(SalaryDateIntent.Confirm) },
            ) {
                Text(
                    text = "완료",
                    style = MoaTheme.typography.t3_700,
                )
            }
        }
    }
}

sealed interface SalaryDateIntent {
    data object ClickBack : SalaryDateIntent

    @JvmInline
    value class ShowSalaryDayBottomSheet(val visible: Boolean) : SalaryDateIntent

    @JvmInline
    value class SetSalaryDay(val day: Int) : SalaryDateIntent

    data object Confirm : SalaryDateIntent
}

@Preview
@Composable
private fun SalaryDateScreenPreview() {
    MoaTheme {
        SalaryDayScreen(
            salaryDay = 25,
            onIntent = {},
        )
    }
}