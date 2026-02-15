package com.moa.app.presentation.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.moa.app.presentation.designsystem.component.MoaHomeTopBar
import com.moa.app.presentation.designsystem.theme.MoaTheme
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.navigation.HomeNavigation
import com.moa.app.presentation.ui.home.afterwork.AfterWorkScreen
import com.moa.app.presentation.ui.home.beforework.BeforeWorkScreen
import com.moa.app.presentation.ui.home.working.WorkingScreen

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val backStack = rememberNavBackStack(HomeNavigation.BeforeWork())

    LaunchedEffect(Unit) {
        viewModel.moaSideEffects.collect {
            when (it) {
                is MoaSideEffect.Navigate -> {
                    when (it.destination) {
                        HomeNavigation.Back -> {
                            if (backStack.size > 1) {
                                backStack.removeAt(backStack.lastIndex)
                            }
                        }

                        is HomeNavigation -> backStack.add(it.destination)

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
            backstack = backStack,
        )
    }
}

@Composable
private fun HomeNavHost(
    modifier: Modifier,
    backstack: NavBackStack<NavKey>,
) {
    NavDisplay(
        modifier = modifier,
        backStack = backstack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
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
}