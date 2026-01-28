package com.moa.app.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    leadingContent: @Composable RowScope.() -> Unit = {},
    subTrailingContent: @Composable RowScope.() -> Unit = {},
    trailingContent: @Composable RowScope.() -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MoaTheme.radius.radius12))
            .background(
                color = MoaTheme.colors.containerPrimary,
                shape = RoundedCornerShape(MoaTheme.radius.radius12),
            )
            .then(modifier)
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
            modifier = Modifier.clickable {},
            leadingContent = {
                Text(
                    text = "Leading Content",
                    color = MoaTheme.colors.textHighEmphasis,
                )
            },
            subTrailingContent = {
                Text(
                    text = "Sub Trailing",
                    color = MoaTheme.colors.textHighEmphasis,
                )
            },
            trailingContent = {
                Text(
                    text = "Trailing",
                    color = MoaTheme.colors.textHighEmphasis,
                )
            }
        )
    }
}