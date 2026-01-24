package com.moa.app.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moa.app.presentation.designsystem.theme.MoaTheme

@Composable
fun MoaRow(
    contentPadding : PaddingValues = PaddingValues(16.dp),
    leadingContent : @Composable () -> Unit,
    subTrailingContent : @Composable () -> Unit,
    trailingContent : @Composable () -> Unit,
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MoaTheme.radius.radius12))
            .background(
                color = MoaTheme.colors.containerPrimary,
                shape = RoundedCornerShape(MoaTheme.radius.radius12),
            )
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leadingContent()

        Spacer(Modifier.weight(1f))

        subTrailingContent()

        Spacer(Modifier.width(MoaTheme.spacing.spacing4))

        trailingContent()
    }
}

@Preview
@Composable
private fun MoaRowPreview() {
    MoaTheme {
        MoaRow(
            leadingContent = {
                Text(text = "Leading Content")
            },
            subTrailingContent = {
                Text(text = "Sub Trailing")
            },
            trailingContent = {
                Text(text = "Trailing")
            }
        )
    }
}