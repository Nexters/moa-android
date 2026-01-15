package com.moa.app.presentation.ui.history

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HistoryScreen(onClick: () -> Unit) {
    Column {
        Text(text = "History Screen")
        Button(onClick = onClick) {
            Text(text = "Go Setting")
        }
    }
}