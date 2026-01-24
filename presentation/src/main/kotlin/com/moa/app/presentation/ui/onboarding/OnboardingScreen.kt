package com.moa.app.presentation.ui.onboarding

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
import com.moa.app.presentation.ui.onboarding.login.LoginScreen
import com.moa.app.presentation.ui.onboarding.nickname.NickNameScreen
import com.moa.app.presentation.ui.onboarding.notification.NotificationScreen
import com.moa.app.presentation.ui.onboarding.salary.SalaryScreen
import com.moa.app.presentation.ui.onboarding.widgetguide.WidgetGuideScreen
import com.moa.app.presentation.ui.onboarding.workplace.WorkPlaceScreen
import com.moa.app.presentation.ui.onboarding.workschedule.WorkScheduleScreen

@Composable
fun OnboardingScreen(viewModel: OnboardingViewModel = hiltViewModel()) {
    val backstack = rememberNavBackStack(OnboardingNavigation.Login)

    LaunchedEffect(Unit) {
        viewModel.moaSideEffects.collect {
            when (it) {
                is MoaSideEffect.Navigate -> {
                    when (it.destination) {
                        OnboardingNavigation.Back -> backstack.removeAt(backstack.size - 1)

                        is OnboardingNavigation -> backstack.add(it.destination)

                        else -> Unit
                    }
                }
            }
        }
    }

    OnboardingNavHost(
        modifier = Modifier.fillMaxSize(),
        backstack = backstack,
    )
}

@Composable
private fun OnboardingNavHost(
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
            entry<OnboardingNavigation.Login> {
                LoginScreen()
            }

            entry<OnboardingNavigation.Nickname> {
                NickNameScreen()
            }

            entry<OnboardingNavigation.WorkPlace> { key ->
                WorkPlaceScreen(args = key.args)
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

            entry<OnboardingNavigation.Notification> {
                NotificationScreen()
            }
        }
    )
}