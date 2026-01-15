package com.moa.app.presentation.ui.splash

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SplashScreen(onClick: () -> Unit) {
    Column {
        Text(text = "Splash Screen")
        Button(onClick = onClick) {
            Text(text = "Go to Onboarding")
        }
    }
}