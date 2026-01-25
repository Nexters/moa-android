package com.moa.app.presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
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
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.navigation.OnboardingNavigation
import com.moa.app.presentation.navigation.RootNavigation
import com.moa.app.presentation.navigation.SettingNavigation
import com.moa.app.presentation.ui.history.HistoryScreen
import com.moa.app.presentation.ui.home.HomeScreen
import com.moa.app.presentation.ui.onboarding.OnboardingScreen
import com.moa.app.presentation.ui.setting.SettingScreen
import com.moa.app.presentation.ui.splash.SplashScreen

@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    val backstack = rememberNavBackStack(RootNavigation.Home)

    LaunchedEffect(Unit) {
        viewModel.moaSideEffects.collect {
            when (it) {
                is MoaSideEffect.Navigate -> {
                    when (it.destination) {
                        is RootNavigation.Back -> backstack.removeAt(backstack.lastIndex)

                        is RootNavigation.Onboarding,
                        is RootNavigation.Home -> {
                            backstack.clear()
                            backstack.add(it.destination)
                        }

                        is OnboardingNavigation -> Unit

                        is SettingNavigation -> Unit

                        else -> backstack.add(it.destination)
                    }
                }
            }
        }
    }

    MainNavHost(
        modifier = Modifier.fillMaxSize(),
        backstack = backstack,
    )
}

@Composable
private fun MainNavHost(
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
            entry<RootNavigation.Splash> {
                SplashScreen()
            }

            entry<RootNavigation.Onboarding> {
                OnboardingScreen()
            }

            entry<RootNavigation.Home> {
                HomeScreen()
            }

            entry<RootNavigation.History> {
                HistoryScreen()
            }

            entry<RootNavigation.Setting> {
                SettingScreen()
            }
        }
    )
}