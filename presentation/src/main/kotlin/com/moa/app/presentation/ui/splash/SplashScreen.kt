package com.moa.app.presentation.ui.splash

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun SplashScreen(viewModel: SplashViewModel = hiltViewModel()) {
    Column {
        Text(text = "Splash Screen")
        Button(onClick = viewModel::emit) {
            Text(text = "Go to Onboarding")
        }
    }
}