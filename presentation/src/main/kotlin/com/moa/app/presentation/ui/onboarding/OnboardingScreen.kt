package com.moa.app.presentation.ui.onboarding

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import com.moa.app.presentation.designsystem.component.MoaNavDisplay
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.model.OnboardingNavigation
import com.moa.app.presentation.ui.onboarding.login.LoginScreen
import com.moa.app.presentation.ui.onboarding.nickname.NicknameScreen
import com.moa.app.presentation.ui.onboarding.salary.SalaryScreen
import com.moa.app.presentation.ui.onboarding.widgetguide.WidgetGuideScreen
import com.moa.app.presentation.ui.onboarding.workschedule.WorkScheduleScreen

@Composable
fun OnboardingScreen(
    startDestination: OnboardingNavigation,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val backStack = rememberNavBackStack(startDestination)

    LaunchedEffect(Unit) {
        viewModel.moaSideEffects.collect {
            when (it) {
                is MoaSideEffect.Navigate -> {
                    when (it.destination) {
                        OnboardingNavigation.Back -> {
                            if (backStack.size == 1) {
                                viewModel.onIntent(OnboardingIntent.RootBack)
                            } else {
                                backStack.removeAt(backStack.lastIndex)
                            }
                        }

                        is OnboardingNavigation -> backStack.add(it.destination)

                        else -> Unit
                    }
                }

                else -> Unit
            }
        }
    }

    OnboardingNavHost(
        modifier = Modifier.fillMaxSize(),
        backStack = backStack,
    )
}

@Composable
private fun OnboardingNavHost(
    modifier: Modifier,
    backStack: NavBackStack<NavKey>,
) {
    MoaNavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryProvider = entryProvider {
            entry<OnboardingNavigation.Login> {
                LoginScreen()
            }

            entry<OnboardingNavigation.Nickname> { key ->
                NicknameScreen(args = key.args)
            }

            entry<OnboardingNavigation.Salary> { key ->
                SalaryScreen(args = key.args)
            }

            entry<OnboardingNavigation.WorkSchedule> { key ->
                WorkScheduleScreen(args = key.args)
            }

            entry<OnboardingNavigation.WidgetGuide> {
                WidgetGuideScreen()
            }
        }
    )
}

sealed interface OnboardingIntent {
    data object RootBack : OnboardingIntent
}