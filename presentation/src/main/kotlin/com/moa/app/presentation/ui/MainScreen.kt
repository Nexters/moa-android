package com.moa.app.presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.moa.app.presentation.navigation.Screen
import com.moa.app.presentation.ui.history.HistoryScreen
import com.moa.app.presentation.ui.home.HomeScreen
import com.moa.app.presentation.ui.onboarding.OnboardingScreen
import com.moa.app.presentation.ui.setting.SettingScreen
import com.moa.app.presentation.ui.splash.SplashScreen

@Composable
fun MainScreen() {
    val backstack = rememberNavBackStack(Screen.Splash)

    Scaffold { innerPadding ->
        MainNavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            backstack = backstack,
        )
    }
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
            entry<Screen.Splash> {
                SplashScreen(onClick = { backstack.add(Screen.Onboarding) })
            }

            entry<Screen.Onboarding> {
                OnboardingScreen(onClick = { backstack.add(Screen.Home) })
            }

            entry<Screen.Home> {
                HomeScreen(onClick = { backstack.add(Screen.History) })
            }

            entry<Screen.History> {
                HistoryScreen(onClick = { backstack.add(Screen.Setting) })
            }

            entry<Screen.Setting> {
                SettingScreen()
            }
        }
    )
}