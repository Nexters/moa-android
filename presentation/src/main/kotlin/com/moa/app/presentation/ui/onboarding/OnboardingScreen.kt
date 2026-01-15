package com.moa.app.presentation.ui.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun OnboardingScreen(onClick: () -> Unit) {
    Column {
        Text(text = "Onboarding Screen")
        Button(onClick = onClick) {
            Text(text = "Go to Home")
        }
    }
}