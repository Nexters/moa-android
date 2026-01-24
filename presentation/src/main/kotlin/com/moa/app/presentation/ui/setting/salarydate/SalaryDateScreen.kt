package com.moa.app.presentation.ui.setting.salarydate

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.component.MoaRow
import com.moa.app.presentation.designsystem.component.MoaTopAppBar
import com.moa.app.presentation.designsystem.theme.MoaTheme

@Composable
fun SalaryDateScreen(viewModel: SalaryDateViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SalaryDateScreen(
        salaryDate = uiState.salaryDate,
        onIntent = viewModel::onIntent,
    )

    if (uiState.showSalaryDateBottomSheet) {

    }
}

@Composable
private fun SalaryDateScreen(
    salaryDate: Int,
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
                            painter = painterResource(R.drawable.icon_back),
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
                text = "언제 월급 받나요?",
                style = MoaTheme.typography.b2_500,
                color = MoaTheme.colors.textMediumEmphasis,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing8))

            MoaRow(
                modifier = Modifier.clickable {
                    onIntent(
                        SalaryDateIntent.ShowSalaryDateBottomSheet(
                            true
                        )
                    )
                },
                leadingContent = {
                    Text(
                        text = "${salaryDate}일",
                        style = MoaTheme.typography.t2_700,
                        color = MoaTheme.colors.textHighEmphasis,
                    )
                },
                trailingContent = {
                    Image(
                        modifier = Modifier.rotate(90f),
                        painter = painterResource(R.drawable.icon_chevron_right),
                        contentDescription = "Chevron Right",
                    )
                }
            )
        }
    }
}

sealed interface SalaryDateIntent {
    data object ClickBack : SalaryDateIntent

    @JvmInline
    value class ShowSalaryDateBottomSheet(val visible: Boolean) : SalaryDateIntent

    @JvmInline
    value class SetSalaryDate(val date: Int) : SalaryDateIntent
}

@Preview
@Composable
private fun SalaryDateScreenPreview() {
    MoaTheme {
        SalaryDateScreen(
            salaryDate = 25,
            onIntent = {},
        )
    }
}