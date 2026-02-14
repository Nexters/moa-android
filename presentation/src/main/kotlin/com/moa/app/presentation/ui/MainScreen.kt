package com.moa.app.presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.moa.app.presentation.designsystem.component.MoaDialog
import com.moa.app.presentation.designsystem.component.MoaFullScreenProgress
import com.moa.app.presentation.model.MoaDialogProperties
import com.moa.app.presentation.model.MoaSideEffect
import com.moa.app.presentation.model.OnboardingNavigation
import com.moa.app.presentation.model.RootNavigation
import com.moa.app.presentation.model.SettingNavigation
import com.moa.app.presentation.ui.history.HistoryScreen
import com.moa.app.presentation.ui.home.HomeScreen
import com.moa.app.presentation.ui.onboarding.OnboardingScreen
import com.moa.app.presentation.ui.setting.SettingScreen
import com.moa.app.presentation.ui.splash.SplashScreen
import com.moa.app.presentation.ui.webview.WebViewScreen

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onFinish: () -> Unit,
) {
    val backstack = rememberNavBackStack(RootNavigation.Splash)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.moaSideEffects.collect {
            when (it) {
                is MoaSideEffect.Navigate -> {
                    focusManager.clearFocus()

                    when (it.destination) {
                        is RootNavigation.Back -> {
                            if (backstack.size > 1) {
                                backstack.removeAt(backstack.lastIndex)
                            } else {
                                onFinish()
                            }
                        }

                        is RootNavigation.Onboarding -> {
                            val startDestination = it.destination.startDestination
                            val shouldClear = when (startDestination) {
                                is OnboardingNavigation.Login -> true
                                is OnboardingNavigation.Nickname -> startDestination.args.isOnboarding
                                else -> false
                            }
                            if (shouldClear) backstack.clear()

                            backstack.add(it.destination)
                        }

                        is RootNavigation.Home -> {
                            backstack.clear()
                            backstack.add(it.destination)
                        }

                        is OnboardingNavigation -> Unit

                        is SettingNavigation -> Unit

                        else -> backstack.add(it.destination)
                    }
                }

                is MoaSideEffect.Dialog -> {
                    viewModel.onIntent(MainIntent.SetDialog(it.dialog))
                }

                is MoaSideEffect.Loading -> {
                    viewModel.onIntent(MainIntent.SetLoading(it.isLoading))
                }

                is MoaSideEffect.Failure -> {
                    // TODO Failure
                }
            }
        }
    }

    MainNavHost(
        modifier = Modifier.fillMaxSize(),
        backstack = backstack,
    )

    uiState.dialog?.let {
        MoaDialog(
            properties = it,
            onDismissRequest = { viewModel.onIntent(MainIntent.SetDialog(null)) },
        )
    }

    if (uiState.isLoading) {
        MoaFullScreenProgress()
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
            entry<RootNavigation.Splash> {
                SplashScreen()
            }

            entry<RootNavigation.Onboarding> { key ->
                OnboardingScreen(key.startDestination)
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

            entry<RootNavigation.Webview> { key ->
                WebViewScreen(url = key.url)
            }
        }
    )
}

sealed interface MainIntent {
    @JvmInline
    value class SetDialog(val dialog: MoaDialogProperties?) : MainIntent

    @JvmInline
    value class SetLoading(val visible: Boolean) : MainIntent
}