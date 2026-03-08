package com.moa.salary.app.presentation.ui.home

import android.Manifest.permission.POST_NOTIFICATIONS
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import com.moa.salary.app.presentation.designsystem.component.MoaHomeTopBar
import com.moa.salary.app.presentation.designsystem.component.MoaNavDisplay
import com.moa.salary.app.presentation.designsystem.component.MoaNotificationBottomSheet
import com.moa.salary.app.presentation.designsystem.theme.MoaTheme
import com.moa.salary.app.presentation.model.HomeNavigation
import com.moa.salary.app.presentation.model.MoaSideEffect
import com.moa.salary.app.presentation.ui.home.afterwork.AfterWorkScreen
import com.moa.salary.app.presentation.ui.home.beforework.BeforeWorkScreen
import com.moa.salary.app.presentation.ui.home.working.WorkingScreen

@Composable
fun HomeScreen(
    startDestination: HomeNavigation,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val backStack = rememberNavBackStack(startDestination)
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { _ -> }
    )

    LaunchedEffect(Unit) {
        viewModel.onIntent(HomeIntent.GetShownNotificationBottomSheet)

        viewModel.moaSideEffects.collect {
            when (it) {
                is MoaSideEffect.Navigate -> {
                    when (it.destination) {
                        is HomeNavigation -> {
                            backStack.clear()
                            backStack.add(it.destination)
                        }

                        else -> Unit
                    }
                }

                else -> Unit
            }
        }
    }

    Scaffold(
        containerColor = MoaTheme.colors.bgPrimary,
        topBar = {
            MoaHomeTopBar(
                onCalendarClick = { viewModel.onIntent(HomeIntent.NavigateToHistory) },
                onSettingClick = { viewModel.onIntent(HomeIntent.NavigateToSetting) },
            )
        }
    ) { innerPadding ->
        HomeNavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            backStack = backStack,
        )
    }

    if (!viewModel.shownNotificationBottomSheet.value) {
        MoaNotificationBottomSheet(
            onAllowClick = {
                viewModel.onIntent(HomeIntent.SetShownNotificationBottomSheet)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissionLauncher.launch(POST_NOTIFICATIONS)
                }
            },
            onDismissRequest = {
                viewModel.onIntent(HomeIntent.SetShownNotificationBottomSheet)
            }
        )
    }
}

@Composable
private fun HomeNavHost(
    modifier: Modifier,
    backStack: NavBackStack<NavKey>,
) {
    MoaNavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryProvider = entryProvider {
            entry<HomeNavigation.BeforeWork> { key ->
                BeforeWorkScreen(args = key)
            }

            entry<HomeNavigation.Working> { key ->
                WorkingScreen(args = key)
            }

            entry<HomeNavigation.AfterWork> { key ->
                AfterWorkScreen(args = key)
            }
        }
    )
}

sealed interface HomeIntent {
    data object NavigateToHistory : HomeIntent
    data object NavigateToSetting : HomeIntent
    data object GetShownNotificationBottomSheet : HomeIntent
    data object SetShownNotificationBottomSheet : HomeIntent
}