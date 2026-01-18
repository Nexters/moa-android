package com.moa.app.presentation.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.theme.MoaTheme

@Composable
fun SplashScreen(viewModel: SplashViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.checkLoginStatus()
    }

    SplashScreen()
}

@Composable
private fun SplashScreen() {
    Scaffold(containerColor = MoaTheme.colors.bgPrimary) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(3f))

            Image(
                painter = painterResource(R.drawable.img_logo),
                contentDescription = "App Logo",
            )

            Spacer(Modifier.weight(4f))
        }
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    MoaTheme {
        SplashScreen()
    }
}