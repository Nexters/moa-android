package com.moa.salary.app.presentation.ui.history

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
import com.moa.salary.app.presentation.model.HistoryNavigation
import com.moa.salary.app.presentation.model.MoaSideEffect
import com.moa.salary.app.presentation.ui.history.calendar.CalendarScreen

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel(),
) {
    val backStack = rememberNavBackStack(HistoryNavigation.Calendar)

    LaunchedEffect(Unit) {
        viewModel.moaSideEffects.collect {
            when (it) {
                is MoaSideEffect.Navigate -> {
                    when (it.destination) {
                        HistoryNavigation.Back -> {
                            if (backStack.size == 1) {
                                viewModel.onIntent(HistoryIntent.RootBack)
                            } else {
                                backStack.removeAt(backStack.lastIndex)
                            }
                        }

                        is HistoryNavigation -> backStack.add(it.destination)

                        else -> Unit
                    }
                }

                else -> Unit
            }
        }
    }

    HistoryNavHost(
        modifier = Modifier.fillMaxSize(),
        backStack = backStack,
    )
}

@Composable
private fun HistoryNavHost(
    modifier: Modifier,
    backStack: NavBackStack<NavKey>,
) {
    MoaNavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryProvider = entryProvider {
            entry<HistoryNavigation.Calendar> {
                CalendarScreen()
            }

            entry<HistoryNavigation.ScheduleForm> { key ->

            }
        },
    )
}

sealed interface HistoryIntent {
    data object RootBack : HistoryIntent
}