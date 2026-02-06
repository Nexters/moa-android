package com.moa.app.presentation.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.theme.MoaTheme

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {

    HomeScreen(
        onIntent = viewModel::onIntent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    onIntent: (HomeIntent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MoaTheme.colors.bgPrimary),
                title = {
                    Text(
                        text = "Moa",
                        style = MoaTheme.typography.t3_500,
                        color = MoaTheme.colors.textHighEmphasis,
                    )
                },
                actions = {
                    IconButton(onClick = {onIntent(HomeIntent.ClickCalendar)}) {
                        Icon(
                            painter = painterResource(R.drawable.ic_24_calendar),
                            contentDescription = "Notification Icon",
                            tint = MoaTheme.colors.textHighEmphasis,
                        )
                    }

                    IconButton(onClick = {onIntent(HomeIntent.ClickSetting)}) {
                        Icon(
                            painter = painterResource(R.drawable.ic_24_setting),
                            contentDescription = "Notification Icon",
                            tint = MoaTheme.colors.textHighEmphasis,
                        )
                    }
                },
            )
        },
        containerColor = MoaTheme.colors.bgPrimary,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {

        }
    }
}

sealed interface HomeIntent {
    data object ClickCalendar : HomeIntent
    data object ClickSetting : HomeIntent
}