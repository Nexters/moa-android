package com.moa.salary.app.presentation.ui.setting

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import com.moa.salary.app.presentation.designsystem.component.MoaNavDisplay
import com.moa.salary.app.presentation.model.MoaSideEffect
import com.moa.salary.app.presentation.model.SettingNavigation
import com.moa.salary.app.presentation.ui.setting.companyname.CompanyNameScreen
import com.moa.salary.app.presentation.ui.setting.menu.SettingMenuScreen
import com.moa.salary.app.presentation.ui.setting.notification.NotificationSettingScreen
import com.moa.salary.app.presentation.ui.setting.salaryday.SalaryDayScreen
import com.moa.salary.app.presentation.ui.setting.terms.TermsScreen
import com.moa.salary.app.presentation.ui.setting.withdraw.WithDrawScreen
import com.moa.salary.app.presentation.ui.setting.workinfo.WorkInfoScreen

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
        backStack = backStack,
    )
}

@Composable
private fun SettingNavHost(
    modifier: Modifier,
    backStack: NavBackStack<NavKey>,
) {
    MoaNavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryProvider = entryProvider {
            entry<SettingNavigation.SettingMenu> {
                SettingMenuScreen()
            }

            entry<SettingNavigation.WorkInfo> {
                WorkInfoScreen()
            }

            entry<SettingNavigation.CompanyName> { key ->
                CompanyNameScreen(companyName = key.companyName)
            }

            entry<SettingNavigation.SalaryDay> { key ->
                SalaryDayScreen(day = key.day)
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