package com.moa.app.presentation.ui.setting.withdraw

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.component.MoaPrimaryButton
import com.moa.app.presentation.designsystem.component.MoaTopAppBar
import com.moa.app.presentation.designsystem.theme.MoaTheme
import com.moa.app.core.model.setting.WithdrawalReason
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun WithDrawScreen(viewModel: WithDrawViewModel = hiltViewModel()) {
    WithDrawScreen(
        selectedReasons = viewModel.selectedReasons,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun WithDrawScreen(
    selectedReasons: ImmutableSet<WithdrawalReason>,
    onIntent: (WithDrawIntent) -> Unit,
) {
    Scaffold(
        topBar = {
            MoaTopAppBar(
                title = {
                    Text(
                        text = "회원 탈퇴",
                        style = MoaTheme.typography.t3_500,
                        color = MoaTheme.colors.textHighEmphasis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onIntent(WithDrawIntent.ClickBack) }) {
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
                text = "헤어지게 되어 아쉬워요..",
                style = MoaTheme.typography.b2_400,
                color = MoaTheme.colors.textMediumEmphasis,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing4))

            Text(
                text = "탈퇴 사유를 알려주시면\n" +
                        "더 나은 서비스를 제공하기 위해\n" +
                        "노력할게요.",
                style = MoaTheme.typography.t1_700,
                color = MoaTheme.colors.textHighEmphasis,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing36))

            WithDrawItems(
                selectedReasons = selectedReasons,
                onIntent = onIntent,
            )

            Spacer(Modifier.weight(1f))

            MoaPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = MoaTheme.spacing.spacing24)
                    .height(64.dp),
                enabled = selectedReasons.isNotEmpty(),
                onClick = { onIntent(WithDrawIntent.ClickWithDraw) },
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
private fun WithDrawItems(
    selectedReasons: ImmutableSet<WithdrawalReason>,
    onIntent: (WithDrawIntent) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(MoaTheme.spacing.spacing24)) {
        WithdrawalReason.entries.forEach {
            WithDrawItem(
                selected = selectedReasons.contains(it),
                reason = it,
                onClick = { reason ->
                    onIntent(WithDrawIntent.ClickReason(reason))
                }
            )
        }
    }
}

@Composable
private fun WithDrawItem(
    selected: Boolean,
    reason: WithdrawalReason,
    onClick: (WithdrawalReason) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(reason) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(
                if (selected) {
                    R.drawable.ic_rectangle_checked
                } else {
                    R.drawable.ic_rectangle_unchecked
                }
            ),
            contentDescription = null,
        )

        Spacer(Modifier.width(MoaTheme.spacing.spacing12))

        Text(
            text = reason.title,
            style = MoaTheme.typography.b1_500,
            color = MoaTheme.colors.textHighEmphasis,
        )
    }
}

sealed interface WithDrawIntent {
    data object ClickBack : WithDrawIntent

    @JvmInline
    value class ClickReason(val reason: WithdrawalReason) : WithDrawIntent

    data object ClickWithDraw : WithDrawIntent
}

@Preview
@Composable
private fun WithDrawScreenPreview() {
    MoaTheme {
        WithDrawScreen()
    }
}