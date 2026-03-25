package com.moa.salary.app.presentation.ui

import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.moa.salary.app.core.exception.ApiErrorException
import com.moa.salary.app.core.exception.NetworkException
import com.moa.salary.app.core.exception.ServerException
import com.moa.salary.app.presentation.designsystem.component.MoaDialog
import com.moa.salary.app.presentation.designsystem.component.MoaErrorScreen
import com.moa.salary.app.presentation.designsystem.component.MoaFullScreenProgress
import com.moa.salary.app.presentation.designsystem.component.MoaNavDisplay
import com.moa.salary.app.presentation.designsystem.theme.MoaTheme
import com.moa.salary.app.presentation.model.HomeNavigation
import com.moa.salary.app.presentation.model.MoaDialogProperties
import com.moa.salary.app.presentation.model.MoaSideEffect
import com.moa.salary.app.presentation.model.OnboardingNavigation
import com.moa.salary.app.presentation.model.RootNavigation
import com.moa.salary.app.presentation.model.SettingNavigation
import com.moa.salary.app.presentation.ui.history.HistoryScreen
import com.moa.salary.app.presentation.ui.home.HomeScreen
import com.moa.salary.app.presentation.ui.onboarding.OnboardingScreen
import com.moa.salary.app.presentation.ui.setting.SettingScreen
import com.moa.salary.app.presentation.ui.splash.SplashScreen
import com.moa.salary.app.presentation.ui.webview.WebViewScreen
import kotlinx.coroutines.delay

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onFinish: () -> Unit,
) {
    val backStack = rememberNavBackStack(RootNavigation.Splash)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val toastAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        viewModel.moaSideEffects.collect {
            when (it) {
                is MoaSideEffect.Navigate -> {
                    focusManager.clearFocus()

                    when (it.destination) {
                        is RootNavigation.Back -> {
                            if (backStack.size > 1) {
                                backStack.removeAt(backStack.lastIndex)
                            } else {
                                onFinish()
                            }
                        }

                        is RootNavigation.Splash -> {
                            backStack.clear()
                            backStack.add(it.destination)
                        }

                        is RootNavigation.Onboarding -> {
                            val startDestination = it.destination.startDestination
                            val shouldClear = when (startDestination) {
                                is OnboardingNavigation.Login -> true
                                is OnboardingNavigation.Nickname -> startDestination.args.isOnboarding
                                else -> false
                            }
                            if (shouldClear) backStack.clear()

                            backStack.add(it.destination)
                        }

                        is RootNavigation.Home -> {
                            backStack.clear()
                            backStack.add(it.destination)
                        }

                        is OnboardingNavigation -> Unit

                        is SettingNavigation -> Unit

                        is HomeNavigation -> Unit

                        else -> backStack.add(it.destination)
                    }
                }

                is MoaSideEffect.Dialog -> {
                    viewModel.onIntent(MainIntent.SetDialog(it.dialog))
                }

                is MoaSideEffect.Toast -> {
                    viewModel.onIntent(MainIntent.SetToast(it.message))
                }

                is MoaSideEffect.Loading -> {
                    viewModel.onIntent(MainIntent.SetLoading(it.isLoading))
                }

                is MoaSideEffect.Failure -> {
                    when (it.exception) {
                        is ServerException -> {
                            viewModel.onIntent(MainIntent.SetErrorRetry(it.onRetry))
                        }

                        is NetworkException -> {
                            viewModel.onIntent(
                                MainIntent.SetDialog(
                                    MoaDialogProperties.Alert(
                                        title = "네트워크 연결이 끊겼어요",
                                        message = "네트워크 연결을 확인한 후 \n" +
                                                "다시 시도해 주세요",
                                        alertText = "다시 시도하기",
                                        onAlert = it.onRetry
                                    )
                                )
                            )
                        }

                        is ApiErrorException -> {
                            val message = it.exception.message
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        }

                        else -> {
                            FirebaseCrashlytics.getInstance().recordException(it.exception)
                            Toast.makeText(context, "일시적인 오류가 발생했어요", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(uiState.toastMessage) {
        if (uiState.toastMessage != null) {
            toastAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween()
            )
            delay(1000)
            toastAlpha.animateTo(
                targetValue = 0f,
                animationSpec = tween()
            )
            viewModel.onIntent(MainIntent.SetToast(null))
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        MainNavHost(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            focusManager.clearFocus()
                        }
                    )
                },
            backStack = backStack,
        )

        Text(
            modifier = Modifier
                .alpha(toastAlpha.value)
                .navigationBarsPadding()
                .padding(bottom = 32.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color = MoaTheme.colors.containerSecondary)
                .padding(
                    horizontal = 20.dp,
                    vertical = 12.dp,
                ),
            text = uiState.toastMessage ?: "",
            style = MoaTheme.typography.b1_400,
            color = Color.White
        )
    }

    uiState.dialog?.let {
        MoaDialog(
            properties = it,
            onDismissRequest = { viewModel.onIntent(MainIntent.SetDialog(null)) },
        )
    }

    if (uiState.isLoading) {
        MoaFullScreenProgress()
    }

    uiState.errorRetry?.let {
        MoaErrorScreen(
            onRetry = {
                it.invoke()
                viewModel.onIntent(MainIntent.SetErrorRetry(null))
            }
        )
    }
}

@Composable
private fun MainNavHost(
    modifier: Modifier,
    backStack: NavBackStack<NavKey>,
) {
    MoaNavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryProvider = entryProvider {
            entry<RootNavigation.Splash> {
                SplashScreen()
            }

            entry<RootNavigation.Onboarding> { key ->
                OnboardingScreen(key.startDestination)
            }

            entry<RootNavigation.Home> { key ->
                HomeScreen(startDestination = key.startDestination)
            }

            entry<RootNavigation.History> {
                HistoryScreen()
            }

//            entry<RootNavigation.ScheduleForm> { key ->
//                ScheduleFormScreen(
//                    initialDate = key.date,
//                    schedule = key.schedule
//                )
//            }

            entry<RootNavigation.Setting> { key ->
                SettingScreen(startDestination = key.startDestination)
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

    @JvmInline
    value class SetErrorRetry(val retry: (() -> Unit)?) : MainIntent

    @JvmInline
    value class SetToast(val message: String?) : MainIntent
}