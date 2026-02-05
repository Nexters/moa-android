package com.moa.app.presentation.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.theme.MoaTheme
import com.moa.app.core.model.onboarding.Term
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoaTermBottomSheet(
    terms: ImmutableList<Term>,
    onClickTerm: (Term) -> Unit,
    onClickArrow: (String) -> Unit,
    onClickButton: () -> Unit,
) {
    MoaBottomSheet(
        onDismissRequest = { },
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = false,
            shouldDismissOnClickOutside = false,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MoaTheme.spacing.spacing20),
            verticalArrangement = Arrangement.spacedBy(MoaTheme.spacing.spacing12)
        ) {
            terms.forEach { term ->
                TermRow(
                    term = term,
                    onClickTerm = onClickTerm,
                    onClickArrow = onClickArrow,
                )

                if (term is Term.All) {
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(
                                top = MoaTheme.spacing.spacing16,
                                bottom = MoaTheme.spacing.spacing4,
                            ),
                        color = MoaTheme.colors.dividerSecondary
                    )
                }
            }

            Spacer(Modifier.height(MoaTheme.spacing.spacing24))

            MoaPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = MoaTheme.spacing.spacing20,
                        bottom = MoaTheme.spacing.spacing24,
                    )
                    .height(64.dp),
                enabled = terms.filter { it is Term.Required }.all { it.checked },
                onClick = onClickButton,
            ) {
                Text(
                    text = "확인",
                    style = MoaTheme.typography.t3_700,
                )
            }
        }
    }
}

@Composable
private fun TermRow(
    term: Term,
    onClickTerm: (Term) -> Unit,
    onClickArrow: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MoaTheme.spacing.spacing4),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clickable { onClickTerm(term) },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(
                    if (term.checked) {
                        R.drawable.icon_circle_checked
                    } else {
                        R.drawable.icon_circle_unchcked
                    }
                ),
                contentDescription = "Term Icon"
            )

            Spacer(Modifier.width(MoaTheme.spacing.spacing8))

            Text(
                text = term.title,
                style = when (term) {
                    is Term.All -> MoaTheme.typography.b1_600
                    else -> MoaTheme.typography.b1_400
                },
                color = MoaTheme.colors.textHighEmphasis,
            )
        }

        when (term) {
            is Term.Required,
            is Term.Optional -> {
                Spacer(Modifier.width(MoaTheme.spacing.spacing8))

                Image(
                    modifier = Modifier.clickable { onClickArrow(term.url) },
                    painter = painterResource(R.drawable.icon_chevron_right),
                    contentDescription = "Chevron Right"
                )
            }

            is Term.All -> Unit
        }
    }
}

@Preview
@Composable
private fun TermBottomSheetPreview() {
    MoaTheme {
        MoaTermBottomSheet(
            terms = persistentListOf(
                Term.All(
                    title = "전체 동의하기",
                    url = "",
                    checked = true,
                ),
                Term.Required(
                    title = "(필수) 서비스 이용 약관 동의",
                    url = "https://www.naver.com",
                    checked = true,
                ),
                Term.Optional(
                    title = "(선택) 서비스 이용 약관 동의",
                    url = "https://www.naver.com",
                    checked = false,
                ),
            ),
            onClickTerm = {},
            onClickArrow = {},
            onClickButton = {},
        )
    }
}

@Preview
@Composable
private fun TermRowPreview() {
    MoaTheme {
        Column {
            TermRow(
                term = Term.All(
                    title = "전체 동의하기",
                    url = "",
                    checked = true,
                ),
                onClickTerm = {},
                onClickArrow = {},
            )

            TermRow(
                term = Term.Required(
                    title = "(필수) 서비스 이용 약관 동의",
                    url = "https://www.naver.com",
                    checked = true,
                ),
                onClickTerm = {},
                onClickArrow = {},
            )

            TermRow(
                term = Term.Optional(
                    title = "(선택) 서비스 이용 약관 동의",
                    url = "https://www.naver.com",
                    checked = false,
                ),
                onClickTerm = {},
                onClickArrow = {},
            )
        }
    }
}