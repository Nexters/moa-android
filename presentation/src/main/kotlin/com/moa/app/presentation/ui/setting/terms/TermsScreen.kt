package com.moa.app.presentation.ui.setting.terms

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.component.MoaRow
import com.moa.app.presentation.designsystem.component.MoaTopAppBar
import com.moa.app.presentation.designsystem.theme.MoaTheme
import com.moa.app.core.model.setting.Terms

@Composable
fun TermsScreen(viewModel: TermsViewModel = hiltViewModel()) {
    TermsScreen(
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun TermsScreen(
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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = MoaTheme.spacing.spacing20),
        ) {
            Spacer(Modifier.height(MoaTheme.spacing.spacing20))

            Terms.entries.forEachIndexed { index, term ->
                MoaRow(
                    modifier = Modifier.clickable { onIntent(TermsIntent.ClickTerm(term)) },
                    leadingContent = {
                        Text(
                            text = term.title,
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

                if (index != Terms.entries.lastIndex) {
                    Spacer(Modifier.height(10.dp))
                }
            }
        }
    }
}

sealed interface TermsIntent {
    data object ClickBack : TermsIntent

    @JvmInline
    value class ClickTerm(val term: Terms) : TermsIntent
}

@Preview
@Composable
private fun TermsScreenPreview() {
    MoaTheme {
        TermsScreen(
            onIntent = {},
        )
    }
}