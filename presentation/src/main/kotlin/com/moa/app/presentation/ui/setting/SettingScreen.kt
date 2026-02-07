package com.moa.app.presentation.ui.setting

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
import com.moa.app.presentation.model.SettingNavigation
import com.moa.app.presentation.ui.setting.menu.SettingMenuScreen
import com.moa.app.presentation.ui.setting.notification.NotificationSettingScreen
import com.moa.app.presentation.ui.setting.salarydate.SalaryDateScreen
import com.moa.app.presentation.ui.setting.terms.TermsScreen
import com.moa.app.presentation.ui.setting.withdraw.WithDrawScreen
import com.moa.app.presentation.ui.setting.workinfo.WorkInfoScreen

@Composable
fun SettingScreen(viewModel: SettingViewModel = hiltViewModel()) {
    val backStack = rememberNavBackStack(SettingNavigation.SettingMenu)

    LaunchedEffect(Unit) {
        viewModel.moaSideEffects.collect {
            when (it) {
                is MoaSideEffect.Navigate -> {
                    when (it.destination) {
                        SettingNavigation.Back -> {
                            if (backStack.size == 1) {
                                viewModel.onIntent(SettingIntent.RootBack)
                            } else {
                                backStack.removeAt(backStack.lastIndex)
                            }
                        }

                        is SettingNavigation -> backStack.add(it.destination)

                        else -> Unit
                    }
                }

                else -> Unit
            }
        }
    }

    SettingNavHost(
        modifier = Modifier.fillMaxSize(),
        backstack = backStack,
    )
}

@Composable
private fun SettingNavHost(
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
            entry<SettingNavigation.SettingMenu> {
                SettingMenuScreen()
            }

            entry<SettingNavigation.WorkInfo> {
                WorkInfoScreen()
            }

            entry<SettingNavigation.SalaryDate> {
                SalaryDateScreen()
            }

            entry<SettingNavigation.NotificationSetting> {
                NotificationSettingScreen()
            }

            entry<SettingNavigation.Terms> {
                TermsScreen()
            }

            entry<SettingNavigation.WithDraw> {
                WithDrawScreen()
            }
        }
    )
}

sealed interface SettingIntent {
    data object RootBack : SettingIntent
}