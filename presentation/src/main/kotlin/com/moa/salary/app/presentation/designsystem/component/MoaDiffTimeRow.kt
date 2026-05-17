package com.moa.salary.app.presentation.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.moa.salary.app.presentation.R
import com.moa.salary.app.presentation.designsystem.theme.MoaTheme

@Composable
fun MoaDiffTimeRow(
    diffTimeString: String,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(R.drawable.ic_16_time),
            contentDescription = null,
        )

        Spacer(Modifier.width(4.dp))

        Text(
            text = "총 ${diffTimeString}근무해요.",
            style = MoaTheme.typography.b2_500,
            color = MoaTheme.colors.textGreen,
        )
    }
}