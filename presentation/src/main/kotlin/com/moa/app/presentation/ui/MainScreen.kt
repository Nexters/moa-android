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
import com.moa.app.presentation.navigation.Screen
import com.moa.app.presentation.ui.history.HistoryScreen
import com.moa.app.presentation.ui.home.HomeScreen
import com.moa.app.presentation.ui.onboarding.login.LoginScreen
import com.moa.app.presentation.ui.onboarding.nickname.NickNameScreen
import com.moa.app.presentation.ui.onboarding.salary.SalaryScreen
import com.moa.app.presentation.ui.onboarding.widgetguide.WidgetGuideScreen
import com.moa.app.presentation.ui.onboarding.workplace.WorkPlaceScreen
import com.moa.app.presentation.ui.onboarding.workschedule.WorkScheduleScreen
import com.moa.app.presentation.ui.setting.SettingScreen
import com.moa.app.presentation.ui.splash.SplashScreen

@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    val backstack = rememberNavBackStack(Screen.WorkSchedule)

    LaunchedEffect(Unit) {
        viewModel.moaSideEffects.collect {
            when (it) {
                is MoaSideEffect.Navigate -> {
                    when (it.destination) {
                        Screen.Back -> backstack.removeAt(backstack.size - 1)

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
            entry<Screen.Splash> {
                SplashScreen()
            }

            entry<Screen.Login> {
                LoginScreen()
            }

            entry<Screen.Nickname> {
                NickNameScreen()
            }

            entry<Screen.WorkPlace> {
                WorkPlaceScreen()
            }

            entry<Screen.Salary> {
                SalaryScreen()
            }

            entry<Screen.WorkSchedule> {
                WorkScheduleScreen()
            }

            entry<Screen.WidgetGuide> {
                WidgetGuideScreen()
            }

            entry<Screen.Home> {
                HomeScreen()
            }

            entry<Screen.History> {
                HistoryScreen()
            }

            entry<Screen.Setting> {
                SettingScreen()
            }
        }
    )
}