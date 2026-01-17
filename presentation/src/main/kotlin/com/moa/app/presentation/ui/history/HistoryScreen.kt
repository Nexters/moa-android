package com.moa.app.presentation.ui.history

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun HistoryScreen(viewModel: HistoryViewModel = hiltViewModel()) {
    Column {
        Text(text = "History Screen")
        Button(onClick = viewModel::emit) {
            Text(text = "Go Setting")
        }
    }
}