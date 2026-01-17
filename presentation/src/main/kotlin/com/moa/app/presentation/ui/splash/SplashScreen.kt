package com.moa.app.presentation.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.moa.app.presentation.designsystem.theme.MoaTheme

@Composable
fun SplashScreen(viewModel: SplashViewModel = hiltViewModel()) {
    Column(modifier = Modifier.background(MoaTheme.colors.bgPrimary)) {
        Text(
            text = "Splash Screen",
            style = MoaTheme.typography.h1_700,
            color = MoaTheme.colors.textHighEmphasis,
        )
        Button(onClick = viewModel::emit) {
            Text(text = "Go to Onboarding")
        }
    }
}