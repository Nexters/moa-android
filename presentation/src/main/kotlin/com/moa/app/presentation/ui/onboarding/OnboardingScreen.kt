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
import com.moa.app.presentation.navigation.OnboardingScreen
import com.moa.app.presentation.ui.onboarding.login.LoginScreen
import com.moa.app.presentation.ui.onboarding.nickname.NickNameScreen
import com.moa.app.presentation.ui.onboarding.notification.NotificationScreen
import com.moa.app.presentation.ui.onboarding.salary.SalaryScreen
import com.moa.app.presentation.ui.onboarding.widgetguide.WidgetGuideScreen
import com.moa.app.presentation.ui.onboarding.workplace.WorkPlaceScreen
import com.moa.app.presentation.ui.onboarding.workschedule.WorkScheduleScreen

@Composable
fun OnboardingScreen(viewModel: OnboardingViewModel = hiltViewModel()) {
    val backstack = rememberNavBackStack(OnboardingScreen.Login)

    LaunchedEffect(Unit) {
        viewModel.moaSideEffects.collect {
            when (it) {
                is MoaSideEffect.Navigate -> {
                    when (it.destination) {
                        OnboardingScreen.Back -> backstack.removeAt(backstack.size - 1)

                        is OnboardingScreen -> backstack.add(it.destination)

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
            entry<OnboardingScreen.Login> {
                LoginScreen()
            }

            entry<OnboardingScreen.Nickname> {
                NickNameScreen()
            }

            entry<OnboardingScreen.WorkPlace> {
                WorkPlaceScreen()
            }

            entry<OnboardingScreen.Salary> {
                SalaryScreen()
            }

            entry<OnboardingScreen.WorkSchedule> {
                WorkScheduleScreen()
            }

            entry<OnboardingScreen.WidgetGuide> {
                WidgetGuideScreen()
            }

            entry<OnboardingScreen.Notification> {
                NotificationScreen()
            }
        }
    )
}