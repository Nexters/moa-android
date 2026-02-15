package com.moa.app.presentation.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.theme.MoaTheme

@Composable
fun MoaHomeTopBar(
    onCalendarClick: () -> Unit,
    onSettingClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MoaTopAppBar(
        navigationIcon = {
            Image(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .width(50.dp)
                    .height(24.dp),
                painter = painterResource(id = R.drawable.img_white_logo),
                contentDescription = "MOA Logo",
            )
        },
        actions = {
            IconButton(onClick = onCalendarClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_24_calendar),
                    contentDescription = "History",
                    tint = MoaTheme.colors.textHighEmphasis,
                )
            }
            IconButton(onClick = onSettingClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_24_setting),
                    contentDescription = "Settings",
                    tint = MoaTheme.colors.textHighEmphasis,
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        },
    )
}

@Preview
@Composable
private fun MoaHomeTopBarPreview() {
    MoaTheme {
        MoaHomeTopBar(
            onCalendarClick = {},
            onSettingClick = {},
        )
    }
}
