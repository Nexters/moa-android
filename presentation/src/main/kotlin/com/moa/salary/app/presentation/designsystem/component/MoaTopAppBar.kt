package com.moa.salary.app.presentation.designsystem.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.moa.salary.app.presentation.designsystem.theme.MoaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoaTopAppBar(
    title: @Composable () -> Unit = {},
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    containerColor : Color = MoaTheme.colors.bgPrimary,
) {
    CenterAlignedTopAppBar(
        title = title,
        navigationIcon = navigationIcon,
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(containerColor = containerColor)
    )
}