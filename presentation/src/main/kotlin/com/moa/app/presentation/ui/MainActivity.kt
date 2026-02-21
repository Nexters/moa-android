package com.moa.app.presentation.ui

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.moa.app.presentation.designsystem.theme.MoaTheme
import com.moa.app.presentation.ui.widget.util.WidgetUpdateManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var widgetUpdateManager: WidgetUpdateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )

        setContent {
            MoaTheme {
                MainScreen(onFinish = { finish() })
            }
        }
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            widgetUpdateManager.updateAllWidgets()
        }
    }
}