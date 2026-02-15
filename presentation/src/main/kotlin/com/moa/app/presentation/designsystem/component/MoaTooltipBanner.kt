package com.moa.app.presentation.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.theme.MoaTheme

@Composable
fun MoaTooltipBanner(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = MoaTheme.colors.containerSecondary,
                    shape = RoundedCornerShape(MoaTheme.radius.radius999),
                )
                .padding(
                    horizontal = MoaTheme.spacing.spacing16,
                    vertical = MoaTheme.spacing.spacing8,
                ),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }

        Image(
            painter = painterResource(R.drawable.icon_tooltip_bottom),
            contentDescription = null,
        )
    }
}

@Composable
fun MoaTooltipBanner(
    text: String,
    modifier: Modifier = Modifier,
) {
    MoaTooltipBanner(
        modifier = modifier,
    ) {
        Text(
            text = text,
            style = MoaTheme.typography.b2_500,
            color = MoaTheme.colors.textHighEmphasis,
        )
    }
}

@Preview
@Composable
private fun MoaTooltipBannerPreview() {
    MoaTheme {
        MoaTooltipBanner(
            text = "09:00 자동 출근 예정",
        )
    }
}