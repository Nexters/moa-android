package com.moa.app.presentation.ui.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun OnboardingScreen(viewModel: OnboardingViewModel = hiltViewModel()) {
    Column {
        Text(text = "Onboarding Screen")
        Button(onClick = viewModel::emit) {
            Text(text = "Go to Home")
        }
    }
}