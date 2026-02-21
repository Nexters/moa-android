package com.moa.app.presentation.ui.setting.terms

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moa.app.core.model.setting.SettingTerm
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.component.MoaRow
import com.moa.app.presentation.designsystem.component.MoaTopAppBar
import com.moa.app.presentation.designsystem.theme.MoaTheme
import kotlinx.collections.immutable.persistentListOf

@Composable
fun TermsScreen(viewModel: TermsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TermsScreen(
        uiState = uiState,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun TermsScreen(
    uiState: TermsUiState,
    onIntent: (TermsIntent) -> Unit,
) {
    Scaffold(
        topBar = {
            MoaTopAppBar(
                title = {
                    Text(
                        text = "약관 및 정책",
                        style = MoaTheme.typography.t3_500,
                        color = MoaTheme.colors.textHighEmphasis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onIntent(TermsIntent.ClickBack) }) {
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
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(MoaTheme.spacing.spacing20),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(uiState.terms) {
                MoaRow(
                    modifier = Modifier.clickable { onIntent(TermsIntent.ClickTerm(it)) },
                    leadingContent = {
                        Text(
                            text = it.title,
                            style = MoaTheme.typography.b1_500,
                            color = MoaTheme.colors.textHighEmphasis,
                        )
                    },
                    trailingContent = {
                        Image(
                            painter = painterResource(R.drawable.ic_24_chevron_right),
                            contentDescription = "Chevron Right",
                        )
                    }
                )
            }
        }
    }
}

sealed interface TermsIntent {
    data object ClickBack : TermsIntent

    @JvmInline
    value class ClickTerm(val term: SettingTerm) : TermsIntent
}

@Preview
@Composable
private fun TermsScreenPreview() {
    MoaTheme {
        TermsScreen(
            uiState = TermsUiState(
                terms = persistentListOf(
                    SettingTerm(
                        title = "이용약관",
                        url = "",
                    ),
                    SettingTerm(
                        title = "개인정보처리방침",
                        url = "",
                    )
                )
            ),
            onIntent = {},
        )
    }
}