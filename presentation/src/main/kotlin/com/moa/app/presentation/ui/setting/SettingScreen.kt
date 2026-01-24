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
import com.moa.app.presentation.navigation.SettingNavigation

@Composable
fun SettingScreen(viewModel: SettingViewModel = hiltViewModel()) {
    val backStack = rememberNavBackStack(SettingNavigation.SettingMenu)

    LaunchedEffect(Unit) {
        viewModel.moaSideEffects.collect {
            when (it) {
                is MoaSideEffect.Navigate -> {
                    when (it.destination) {
                        SettingNavigation.Back -> backStack.removeAt(backStack.size - 1)

                        is SettingNavigation -> backStack.add(it.destination)

                        else -> Unit
                    }
                }
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

        }
    )
}