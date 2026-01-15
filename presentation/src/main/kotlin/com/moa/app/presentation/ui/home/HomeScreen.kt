package com.moa.app.presentation.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeScreen(onClick: () -> Unit) {
    Column {
        Text(text = "Home Screen")
        Button(onClick = onClick) {
            Text(text = "Go to History")
        }
    }
}