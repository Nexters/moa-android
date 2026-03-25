package com.moa.salary.app.presentation.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moa.salary.app.presentation.R
import com.moa.salary.app.presentation.designsystem.theme.MoaTheme

@Composable
fun MoaMonthNavigator(
    month: Int,
    previousEnabled : Boolean,
    nextEnabled : Boolean,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(
            modifier = Modifier
                .clip(CircleShape)
                .clickable(
                    enabled = previousEnabled,
                    onClick = onPreviousClick,
                ),
            painter = painterResource(R.drawable.ic_24_chevron_left),
            contentDescription = stringResource(R.string.history_previous_month_description),
            tint = if(previousEnabled) {
                MoaTheme.colors.textHighEmphasis
            }else{
                MoaTheme.colors.textDisabled
            }
        )

        Text(
            text = stringResource(R.string.history_month_format, month),
            style = MoaTheme.typography.t2_500,
            color = MoaTheme.colors.textHighEmphasis,
        )

        Icon(
            modifier = Modifier
                .clip(CircleShape)
                .clickable(
                    enabled = nextEnabled,
                    onClick = onNextClick,
                ),
            painter = painterResource(R.drawable.ic_24_chevron_right),
            contentDescription = stringResource(R.string.history_next_month_description),
            tint = if(nextEnabled) {
                MoaTheme.colors.textHighEmphasis
            }else{
                MoaTheme.colors.textDisabled
            }
        )
    }
}