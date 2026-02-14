package com.moa.app.presentation.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.theme.MoaTheme

@Composable
fun MoaErrorScreen(
    onRetry: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MoaTheme.colors.bgPrimary),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(R.drawable.img_error),
                contentDescription = "Error"
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing24))

            Text(
                text = "일시적인 오류가 발생했어요",
                style = MoaTheme.typography.t2_700,
                color = MoaTheme.colors.textHighEmphasis,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing4))

            Text(
                text = "서버에 오류가 발생했어요.\n" +
                        "다시 시도 해주세요.",
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textMediumEmphasis,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing24))

            MoaPrimaryButton(
                modifier = Modifier
                    .width(148.dp)
                    .height(56.dp),
                onClick = onRetry,
            ) {
                Text(
                    text = "다시 시도하기",
                    style = MoaTheme.typography.t3_700,
                )
            }
        }
    }
}

@Preview
@Composable
private fun MoaErrorScreenPreview() {
    MoaTheme {
        MoaErrorScreen(
            onRetry = {}
        )
    }
}