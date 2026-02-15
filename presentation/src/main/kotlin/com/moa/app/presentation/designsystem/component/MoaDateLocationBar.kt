package com.moa.app.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.theme.MoaTheme

@Composable
fun MoaDateLocationBar(
    date: String,
    location: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(
                color = MoaTheme.colors.containerPrimary,
                shape = RoundedCornerShape(MoaTheme.radius.radius999),
            )
            .padding(
                horizontal = MoaTheme.spacing.spacing16,
                vertical = MoaTheme.spacing.spacing12,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            painter = painterResource(R.drawable.icon_time),
            contentDescription = stringResource(R.string.date_location_bar_date_icon_description),
            tint = MoaTheme.colors.textHighEmphasis,
        )

        Spacer(Modifier.width(4.dp))

        Text(
            text = date,
            style = MoaTheme.typography.b2_500,
            color = MoaTheme.colors.textHighEmphasis,
        )

        Spacer(Modifier.width(MoaTheme.spacing.spacing12))

        Icon(
            modifier = Modifier.size(16.dp),
            painter = painterResource(R.drawable.icon_location),
            contentDescription = stringResource(R.string.date_location_bar_location_icon_description),
            tint = MoaTheme.colors.textHighEmphasis,
        )

        Spacer(Modifier.width(4.dp))

        Text(
            text = location,
            style = MoaTheme.typography.b2_500,
            color = MoaTheme.colors.textHighEmphasis,
        )
    }
}

@Preview
@Composable
private fun MoaDateLocationBarPreview() {
    MoaTheme {
        MoaDateLocationBar(
            date = "2월 15일 (토)",
            location = "모아주식회사",
        )
    }
}