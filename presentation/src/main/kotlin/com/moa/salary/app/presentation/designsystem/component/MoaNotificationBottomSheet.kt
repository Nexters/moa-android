package com.moa.salary.app.presentation.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.moa.salary.app.presentation.designsystem.theme.MoaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoaNotificationBottomSheet(
    onAllowClick: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    MoaBottomSheet(
        onDismissRequest = onDismissRequest,
    ) {
        MoaNotificationBottomSheetContent(
            onAllowClick = onAllowClick,
            onDismissRequest = onDismissRequest,
        )
    }
}

@Composable
private fun MoaNotificationBottomSheetContent(
    onAllowClick: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MoaTheme.spacing.spacing20)
    ) {
        Text(
            text = "근무 알림을 받아보세요!",
            style = MoaTheme.typography.t1_700,
            color = MoaTheme.colors.textHighEmphasis,
        )

        Spacer(Modifier.height(MoaTheme.spacing.spacing16))

        Text(
            text = "알림 수신에 동의하면, 출퇴근 시간과 월급일에\n" +
                    "알림을 보내드릴게요!",
            style = MoaTheme.typography.b1_500,
            color = MoaTheme.colors.textHighEmphasis,
        )

        Spacer(Modifier.height(MoaTheme.spacing.spacing24))
        Spacer(Modifier.height(MoaTheme.spacing.spacing20))

        MoaPrimaryButton(
            onClick = onAllowClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "알림 받을게요",
                style = MoaTheme.typography.t3_700,
                color = MoaTheme.colors.textHighEmphasisReverse,
            )
        }

        Spacer(Modifier.height(MoaTheme.spacing.spacing16))

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onDismissRequest() },
            text = "다음에 할게요",
            style = MoaTheme.typography.b1_600,
            color = MoaTheme.colors.textMediumEmphasis,
            textDecoration = TextDecoration.Underline,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(MoaTheme.spacing.spacing24))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun MoaNotificationBottomSheetPreview() {
    MoaTheme {
        MoaNotificationBottomSheetContent(
            onAllowClick = {},
            onDismissRequest = {},
        )
    }
}